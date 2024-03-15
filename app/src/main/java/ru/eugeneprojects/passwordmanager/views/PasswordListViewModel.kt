package ru.eugeneprojects.passwordmanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.paging.PasswordPagingSource
import javax.inject.Inject

@HiltViewModel
class PasswordListViewModel @Inject constructor (
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