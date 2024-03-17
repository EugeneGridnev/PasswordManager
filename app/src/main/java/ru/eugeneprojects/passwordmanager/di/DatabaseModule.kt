package ru.eugeneprojects.passwordmanager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.eugeneprojects.passwordmanager.data.room.PasswordDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun createDAO(passwordDatabase: PasswordDatabase) = passwordDatabase.getPasswordDao()

    @Provides
    fun createDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            PasswordDatabase::class.java,
            "password_db.db"
        ).build()
}