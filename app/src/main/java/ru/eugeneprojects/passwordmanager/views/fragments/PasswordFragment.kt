package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepositoryIMPL
import ru.eugeneprojects.passwordmanager.data.room.PasswordDao
import ru.eugeneprojects.passwordmanager.data.room.PasswordDatabase
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordBinding


class PasswordFragment : Fragment() {

    private var binding: FragmentPasswordBinding? = null
    private lateinit var dao: PasswordDao
    private lateinit var repository: PasswordRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dao = PasswordDatabase(requireContext().applicationContext).getPasswordDao()
        repository = PasswordRepositoryIMPL(dao)
        binding = FragmentPasswordBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnSaveButtonClick()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setOnSaveButtonClick() {
        binding?.savePasswordButton?.setOnClickListener { savePassword() }
    }

    private fun savePassword() {
        lifecycleScope.launch {
            repository.insert(Password(
                0,
                binding?.editTextSiteName?.text.toString(),
                binding?.editTextSiteUrl?.text.toString(),
                binding?.editTextSitePassword?.text.toString(),
            ))
        }
    }
}