package ru.eugeneprojects.passwordmanager.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ru.eugeneprojects.passwordmanager.data.models.Password

@Dao
interface PasswordDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(password: Password): Long

    //suspend fun delete(passwordId: Int)

    @Query("SELECT * FROM table_password ORDER BY passwordId ASC LIMIT :limit OFFSET :offset")
    suspend fun getPasswords(limit: Int, offset: Int): List<Password>
}