from fastapi import FastAPI, HTTPException
import pandas as pd
import datetime
import os
import logging
import uvicorn

# Konfigurasi logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load dataset
try:
    dataset_path = "/app/combined_data.csv"  # Pastikan jalur file benar untuk Cloud Run
    data = pd.read_csv(dataset_path)
    logger.info("Dataset berhasil dimuat!")
except FileNotFoundError:
    logger.error(f"Dataset tidak ditemukan di lokasi {dataset_path}")
    raise RuntimeError("Dataset tidak ditemukan. Pastikan file tersedia di direktori yang benar.")

# Validasi kolom dataset
expected_columns = ["date", "province_name", "Tavg", "RH_avg", "RR", "ss", "ff_avg"]
data.columns = data.columns.str.strip()  # Hilangkan spasi pada nama kolom
missing_columns = [col for col in expected_columns if col not in data.columns]
if missing_columns:
    logger.error(f"Kolom berikut tidak ditemukan di dataset: {missing_columns}")
    raise RuntimeError(f"Kolom berikut tidak ditemukan di dataset: {missing_columns}")

# Konversi kolom 'date' menjadi tipe datetime dengan format day-month-year
try:
    data['date'] = pd.to_datetime(data['date'], dayfirst=True)
except Exception as e:
    logger.error(f"Kesalahan saat memproses kolom 'date': {e}")
    raise RuntimeError(f"Kesalahan saat memproses kolom 'date': {e}")

# Inisialisasi aplikasi FastAPI
app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "Welcome to SmartFarm Weather Prediction API"}

@app.get("/predict/{province_name}")
def predict_weather(province_name: str):
    try:
        if province_name not in data['province_name'].unique():
            logger.error(f"Provinsi {province_name} tidak ditemukan.")
            raise HTTPException(status_code=404, detail="Provinsi tidak ditemukan dalam data historis.")
        
        logger.info(f"Memulai prediksi untuk provinsi {province_name}.")
        
        # Data Dummy
        weather_conditions = ["Cerah", "Berawan", "Hujan Ringan", "Hujan Sedang", "Hujan Lebat"]
        today = datetime.date.today()
        forecast = []
        
        for i in range(7):  # Prediksi untuk 7 hari ke depan
            day = (today + datetime.timedelta(days=i)).strftime("%A")
            forecast.append({
                "day": day,
                "temperature": 30.0,  # Dummy temperature
                "humidity": 80.0,  # Dummy humidity
                "rainfall": 0.0,  # Dummy rainfall
                "sunshine_duration": 8.0,  # Dummy sunshine duration
                "wind_speed": 5.0,  # Dummy wind speed
                "condition": weather_conditions[0]  # Semua "Cerah"
            })
        
        return {"province": province_name, "forecast": forecast}
    except Exception as e:
        logger.error(f"Error saat memproses prediksi: {e}")
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8000))  # Default ke 8000
    uvicorn.run(app, host="0.0.0.0", port=port)