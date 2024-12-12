package com.tyas.smartfarm.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    /**
     * Menyisipkan atau memperbarui pengguna dalam tabel.
     * Jika ada konflik (seperti id yang sama), data akan diperbarui.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)

    /**
     * Mengambil data pengguna pertama dari tabel.
     */
    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): LiveData<User>

    /**
     * Menghapus semua data dari tabel pengguna.
     * Berguna jika ingin mereset profil pengguna.
     */
    @Query("DELETE FROM user_table")
    suspend fun clearUser()
}