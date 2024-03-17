package ru.eugeneprojects.passwordmanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.eugeneprojects.passwordmanager.data.SharedPreferenceManager
import ru.eugeneprojects.passwordmanager.data.encryption.CryptoManager
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.paging.PasswordPagingSource
import javax.inject.Inject

@HiltViewModel
class PasswordSharedViewModel @Inject constructor (
    private val repository: PasswordRepository,
    private val cryptoManager:CryptoManager,
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModel() {

    val masterPassword: String?
        get() = sharedPreferenceManager.getMasterPasswordExistInPref()

    private var updateStateFlow = MutableStateFlow(Any())

    val data = updateStateFlow.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
            ),
        ) {
            PasswordPagingSource(repository)
        }.flow.cachedIn(viewModelScope)
    }
    suspend fun updatePassword(passwordId: Int, passwordName: String, passwordUrl: String, passwordValue: String) {

        repository.update(Password(
            passwordId,
            passwordName,
            passwordUrl,
            cryptoManager.encrypt(passwordValue)
        ))
        updateStateFlow.value = Any()
    }

    suspend fun addPassword(passwordId: Int, passwordName: String, passwordUrl: String, passwordValue: String) {

        repository.insert(Password(
            passwordId,
            passwordName,
            passwordUrl,
            cryptoManager.encrypt(passwordValue)
        ))
        updateStateFlow.value = Any()

    }

    fun decrypt(data: String) = cryptoManager.decrypt(data)

    suspend fun deleteItem(item: Password) {
        repository.delete(item)
        updateStateFlow.value = Any()

    }

    fun setMasterPassword(value: String) = sharedPreferenceManager.saveMasterPasswordInPref(value)

    fun isMasterPasswordExists() = sharedPreferenceManager.isMasterPasswordExistInPref()

    companion object {
        private const val PAGE_SIZE = 20
        private const val INITIAL_LOAD_SIZE = 20
        private const val PREFETCH_DISTANCE = 20 / 2
    }

}