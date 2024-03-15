package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.eugeneprojects.passwordmanager.data.models.Password
import ru.eugeneprojects.passwordmanager.data.repository.PasswordRepository
import ru.eugeneprojects.passwordmanager.databinding.FragmentPasswordBinding
import javax.inject.Inject

@AndroidEntryPoint
class PasswordFragment : Fragment() {

    private var binding: FragmentPasswordBinding? = null
    private val args: PasswordFragmentArgs by navArgs()
    @Inject lateinit var repository: PasswordRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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