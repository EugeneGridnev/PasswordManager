package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import ru.eugeneprojects.passwordmanager.views.dialogs.ChangeMasterPasswordDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.FirstTimeDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.MasterPasswordDialogFragment

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

        showFirstTimeAccessDialog()

        setAddNewPasswordFAB()

        setSettingsOnClickListener()

        val adapter = PasswordPagingAdapter()
        binding?.recyclerViewPasswordsList?.adapter = adapter.withLoadStateFooter(
            PasswordLoadStateAdapter()
        )

        setOnListItemClick(adapter)

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

    private fun setOnListItemClick(adapter: PasswordPagingAdapter) {
        adapter.setOnItemClickListener {
            showItemAccessDialog()
//            val bundle = Bundle().apply {
//                putParcelable("password", it)
//            }
//            findNavController().navigate(
//                R.id.action_passwordListFragment_to_passwordFragment,
//                bundle
//            )
        }

        binding?.fabAddPassword?.setOnClickListener {
            findNavController().navigate(R.id.action_passwordListFragment_to_passwordFragment)
        }
    }

    private fun setSettingsOnClickListener() {
        binding?.masterPassSettingIcon?.setOnClickListener {
            showChangeMasterPasswordDialog()
        }
    }

    private fun showChangeMasterPasswordDialog() {
        ChangeMasterPasswordDialogFragment.show(parentFragmentManager)
    }

    private fun showItemAccessDialog() {
        MasterPasswordDialogFragment.show(parentFragmentManager)
    }

    private fun showFirstTimeAccessDialog() {
        FirstTimeDialogFragment.show(parentFragmentManager)
    }


}