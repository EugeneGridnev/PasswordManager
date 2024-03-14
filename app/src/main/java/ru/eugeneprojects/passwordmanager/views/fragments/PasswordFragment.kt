package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private val args: PasswordFragmentArgs by navArgs()
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


        setUpPasswordUI()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setUpPasswordUI() {
        if (args.password != null) {
            setUpPasswordDataToChange()
            binding?.savePasswordButton?.setOnClickListener {
                updatePassword()
            }
        } else {
            binding?.savePasswordButton?.setOnClickListener {
                addPassword()
            }
        }
    }

    private fun addPassword() {
        lifecycleScope.launch {
            repository.insert(Password(
                0,
                binding?.editTextSiteName?.text.toString(),
                binding?.editTextSiteUrl?.text.toString(),
                binding?.editTextSitePassword?.text.toString()
            ))
            findNavController().popBackStack()
        }
    }

    private fun updatePassword() {
        lifecycleScope.launch {
            repository.update(Password(
                args.password!!.passwordId,
                binding?.editTextSiteName?.text.toString(),
                binding?.editTextSiteUrl?.text.toString(),
                binding?.editTextSitePassword?.text.toString()
            ))
            findNavController().popBackStack()
        }
    }

    private fun setUpPasswordDataToChange() {
        binding?.editTextSiteName?.setText(args.password?.passwordSiteName)
        binding?.editTextSiteUrl?.setText(args.password?.passwordSiteUrl)
        binding?.editTextSitePassword?.setText(args.password?.password)
    }
}