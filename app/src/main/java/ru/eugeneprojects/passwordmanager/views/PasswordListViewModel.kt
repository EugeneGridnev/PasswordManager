package ru.eugeneprojects.passwordmanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.data.models.Password
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

    suspend fun updatePassword(password: Password) = repository.update(password)

    suspend fun addPassword(password: Password) = repository.insert(password)

}