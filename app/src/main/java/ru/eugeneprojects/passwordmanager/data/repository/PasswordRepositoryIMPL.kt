package ru.eugeneprojects.passwordmanager.data.repository

import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.room.PasswordDao

class PasswordRepositoryIMPL(private val dao: PasswordDao) : PasswordRepository {
    override suspend fun insert(password: Password) {
        dao.insert(password)
    }

    override suspend fun delete(password: Password) {

    }

    override suspend fun update(password: Password) {
        dao.update(password)
    }

    override suspend fun getPasswords(limit: Int, offset: Int): List<Password> {
        return dao.getPasswords(limit, offset)
    }
}