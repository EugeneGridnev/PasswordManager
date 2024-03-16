package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordBinding
import ru.eugeneprojects.passwordmanager.views.PasswordListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PasswordFragment : Fragment() {

    private var binding: FragmentPasswordBinding? = null
    private val args: PasswordFragmentArgs by navArgs()
    private lateinit var viewModel: PasswordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PasswordListViewModel::class.java]

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

            viewModel.addPassword(Password(
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

            viewModel.updatePassword(Password(
                args.password!!.passwordId,
                binding?.editTextSiteName?.text.toString(),
                binding?.editTextSiteUrl?.text.toString(),
                binding?.editTextSitePassword?.text.toString()
            ))
            findNavController().popBackStack()
        }
    }

    private fun setUpPasswordDataToChange() {
        //TODO во вью модель расшифоровки
        binding?.editTextSiteName?.setText(args.password?.passwordSiteName)
        binding?.editTextSiteUrl?.setText(args.password?.passwordSiteUrl)
        binding?.editTextSitePassword?.setText(args.password?.password)
    }
}