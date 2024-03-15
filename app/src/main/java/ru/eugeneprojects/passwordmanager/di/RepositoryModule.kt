package ru.eugeneprojects.passwordmanager.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepositoryIMPL

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesConnectivityRepository(repositoryIMPL: PasswordRepositoryIMPL) : PasswordRepository

}