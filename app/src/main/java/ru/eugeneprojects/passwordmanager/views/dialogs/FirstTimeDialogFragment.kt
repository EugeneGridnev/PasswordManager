package ru.eugeneprojects.passwordmanager.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.eugeneprojects.passwordmanager.R

class FirstTimeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.change_master_password_title)
            .setMessage(R.string.first_time_dialog_message)
            .setView(R.layout.master_password_edit_text)
            .setPositiveButton(R.string.ok_dialog_button_text, null)
            .create()
    }

    companion object {
        @JvmStatic private val TAG = FirstTimeDialogFragment::class.java.simpleName
//        @JvmStatic private val KEY_COLOR_RESPONSE = "KEY_COLOR_RESPONSE"
//        @JvmStatic private val ARG_COLOR = "ARG_COLOR"
//
//        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager) {
            val dialogFragment = FirstTimeDialogFragment()
            dialogFragment.show(manager, TAG)
        }
    }
}