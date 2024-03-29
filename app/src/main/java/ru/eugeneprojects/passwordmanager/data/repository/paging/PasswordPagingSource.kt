package ru.eugeneprojects.passwordmanager.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository

class PasswordPagingSource (
    private val repository: PasswordRepository
) : PagingSource<Int, Password>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Password> {
        val page = params.key ?: 0

        return try {
            val entities = repository.getPasswords(params.loadSize, page * params.loadSize)

            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Password>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}