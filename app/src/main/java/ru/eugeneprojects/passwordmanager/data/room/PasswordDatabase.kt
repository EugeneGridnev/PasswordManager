package ru.eugeneprojects.passwordmanager.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.eugeneprojects.passwordmanager.data.models.Password

@Database(
    version = 1,
    entities = [ Password::class ]
)
abstract class PasswordDatabase : RoomDatabase() {

    abstract fun getPasswordDao(): PasswordDao
}