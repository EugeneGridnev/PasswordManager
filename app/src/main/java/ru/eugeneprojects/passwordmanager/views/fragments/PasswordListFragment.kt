package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.adapters.PasswordLoadStateAdapter
import ru.eugeneprojects.passwordmanager.adapters.PasswordPagingAdapter
import ru.eugeneprojects.passwordmanager.data.SharedPreferenceManager
import ru.eugeneprojects.passwordmanager.data.models.Password
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
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private val viewModel: PasswordListViewModel by viewModels { PasswordViewModelFactory(repository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dao = PasswordDatabase(requireContext()).getPasswordDao()
        repository = PasswordRepositoryIMPL(dao)
        sharedPreferenceManager = SharedPreferenceManager(requireContext())

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
            showItemAccessDialog(it)
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

    private fun showItemAccessDialog(item: Password) {
        MasterPasswordDialogFragment.show(parentFragmentManager, sharedPreferenceManager.getMasterPasswordExistInPref().toString())
        MasterPasswordDialogFragment.setUpListener(parentFragmentManager, this) {
            if (it) {
                    val bundle = Bundle().apply {
                        putParcelable("password", item)
                }
                findNavController().navigate(
                    R.id.action_passwordListFragment_to_passwordFragment,
                    bundle
                )
            }
        }
    }

    private fun showFirstTimeAccessDialog() {

        if (!sharedPreferenceManager.isMasterPasswordExistInPref()) {
            FirstTimeDialogFragment.show(parentFragmentManager)
            setUpFirstTimeDialogFragmentListener()
        }
    }

    private fun setUpFirstTimeDialogFragmentListener() {
        FirstTimeDialogFragment.setUpListener(parentFragmentManager, this) {
            sharedPreferenceManager.saveMasterPasswordInPref(it)
            Toast.makeText(context, R.string.master_password_created_toast, Toast.LENGTH_SHORT).show()
        }
    }
}