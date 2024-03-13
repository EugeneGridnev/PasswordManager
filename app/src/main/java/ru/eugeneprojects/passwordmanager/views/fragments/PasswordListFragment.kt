package ru.eugeneprojects.passwordmanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.views.PasswordListViewModel

class PasswordListFragment : Fragment() {


    private lateinit var viewModel: PasswordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_list, container, false)
    }


}