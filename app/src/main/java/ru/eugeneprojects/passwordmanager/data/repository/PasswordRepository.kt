package ru.eugeneprojects.passwordmanager.data.repository

import ru.eugeneprojects.passwordmanager.data.models.Password

interface PasswordRepository {

    suspend fun insert(password: Password)

    suspend fun delete(password: Password)

    suspend fun getPasswords(limit: Int, offset: Int): List<Password>
}