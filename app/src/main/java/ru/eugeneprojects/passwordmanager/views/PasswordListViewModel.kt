package ru.eugeneprojects.passwordmanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.paging.PasswordPagingSource

class PasswordListViewModel (
    private val repository: PasswordRepository
) : ViewModel() {

    val data = Pager(
        PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 20 / 2,
            enablePlaceholders = false,
        ),
    ) {
        PasswordPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}

class PasswordViewModelFactory(
    private val repository: PasswordRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PasswordListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}