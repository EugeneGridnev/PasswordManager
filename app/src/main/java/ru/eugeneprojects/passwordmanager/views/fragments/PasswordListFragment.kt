package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.adapters.PasswordLoadStateAdapter
import ru.eugeneprojects.passwordmanager.adapters.PasswordPagingAdapter
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepositoryIMPL
import ru.eugeneprojects.passwordmanager.data.room.PasswordDao
import ru.eugeneprojects.passwordmanager.data.room.PasswordDatabase
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordListBinding
import ru.eugeneprojects.passwordmanager.views.PasswordListViewModel
import ru.eugeneprojects.passwordmanager.views.PasswordViewModelFactory

class PasswordListFragment : Fragment() {

    private var binding: FragmentPasswordListBinding? = null

    private lateinit var dao: PasswordDao
    private lateinit var repository: PasswordRepository
    private val viewModel: PasswordListViewModel by viewModels { PasswordViewModelFactory(repository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = PasswordDatabase(requireContext()).getPasswordDao()
        repository = PasswordRepositoryIMPL(dao)
        binding = FragmentPasswordListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddNewPasswordFAB()

        val adapter = PasswordPagingAdapter()
        binding?.recyclerViewPasswordsList?.adapter = adapter.withLoadStateFooter(
            PasswordLoadStateAdapter()
        )

        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setAddNewPasswordFAB() {

        binding?.fabAddPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_passwordListFragment_to_passwordFragment)
        }
    }

}