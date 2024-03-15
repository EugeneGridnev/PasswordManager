package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.adapters.PasswordLoadStateAdapter
import ru.eugeneprojects.passwordmanager.adapters.PasswordPagingAdapter
import ru.eugeneprojects.passwordmanager.data.SharedPreferenceManager
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordListBinding
import ru.eugeneprojects.passwordmanager.views.PasswordListViewModel
import ru.eugeneprojects.passwordmanager.views.dialogs.ChangeMasterPasswordDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.FirstTimeDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.MasterPasswordDialogFragment
import javax.inject.Inject

@AndroidEntryPoint
class PasswordListFragment : Fragment() {

    private var binding: FragmentPasswordListBinding? = null

    private lateinit var viewModel: PasswordListViewModel

    @Inject lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PasswordListViewModel::class.java]

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
        ChangeMasterPasswordDialogFragment.show(parentFragmentManager,
            sharedPreferenceManager.getMasterPasswordExistInPref()!!
        )
        ChangeMasterPasswordDialogFragment.setUpListener(parentFragmentManager, this){
            sharedPreferenceManager.saveMasterPasswordInPref(it)
            Toast.makeText(context, R.string.master_password_changed_toast, Toast.LENGTH_SHORT).show()
        }
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