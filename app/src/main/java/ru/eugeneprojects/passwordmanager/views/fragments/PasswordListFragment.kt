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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.adapters.PasswordLoadStateAdapter
import ru.eugeneprojects.passwordmanager.adapters.PasswordPagingAdapter
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordListBinding
import ru.eugeneprojects.passwordmanager.views.PasswordSharedViewModel
import ru.eugeneprojects.passwordmanager.views.dialogs.ChangeMasterPasswordDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.FirstTimeDialogFragment
import ru.eugeneprojects.passwordmanager.views.dialogs.MasterPasswordDialogFragment

@AndroidEntryPoint
class PasswordListFragment : Fragment() {

    private var binding: FragmentPasswordListBinding? = null

    private lateinit var adapter: PasswordPagingAdapter
    private lateinit var viewModel: PasswordSharedViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PasswordSharedViewModel::class.java]

        showFirstTimeAccessDialog()

        setAddNewPasswordFAB()

        setSettingsOnClickListener()

        adapter = PasswordPagingAdapter()
        binding?.recyclerViewPasswordsList?.adapter = adapter.withLoadStateFooter(
            PasswordLoadStateAdapter()
        )

        setOnListItemClick(adapter)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder is PasswordPagingAdapter.PasswordViewHolder) {
                    viewHolder.item?.let { item ->
                        lifecycleScope.launch {
                            viewModel.deleteItem(item)
                        }
                    }
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(binding?.recyclerViewPasswordsList)

        observeData(adapter)

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
            viewModel.masterPassword!!
        )
        ChangeMasterPasswordDialogFragment.setUpListener(parentFragmentManager, this){
            viewModel.setMasterPassword(it)
            Toast.makeText(context, R.string.master_password_changed_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showItemAccessDialog(item: Password) {
        MasterPasswordDialogFragment.show(parentFragmentManager, viewModel.masterPassword?: "")
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

        if (!viewModel.isMasterPasswordExists()) {
            FirstTimeDialogFragment.show(parentFragmentManager)
            setUpFirstTimeDialogFragmentListener()
        }
    }

    private fun setUpFirstTimeDialogFragmentListener() {
        FirstTimeDialogFragment.setUpListener(parentFragmentManager, this) {
            viewModel.setMasterPassword(it)
            Toast.makeText(context, R.string.master_password_created_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData(adapter: PasswordPagingAdapter) {
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}