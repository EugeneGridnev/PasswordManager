package ru.eugeneprojects.passwordmanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import ru.eugeneprojects.passwordmanager.data.repository.paging.PasswordPagingSource
import ru.eugeneprojects.passwordmanager.data.room.PasswordDao

class PasswordListViewModel (
    private val dao: PasswordDao
) : ViewModel() {

    val data = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
    ) {
        PasswordPagingSource(dao)
    }.flow.cachedIn(viewModelScope)
}

class PasswordViewModelFactory(
    private val dao: PasswordDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PasswordListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordListViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}