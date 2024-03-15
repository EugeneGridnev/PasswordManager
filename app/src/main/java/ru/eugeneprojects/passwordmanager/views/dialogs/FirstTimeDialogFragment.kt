package ru.eugeneprojects.passwordmanager.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.databinding.MasterPasswordEditTextBinding

class FirstTimeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogInputBinding = MasterPasswordEditTextBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(R.string.change_master_password_title)
            .setMessage(R.string.first_time_dialog_message)
            .setView(dialogInputBinding.root)
            .setPositiveButton(R.string.ok_dialog_button_text, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogInputBinding.editTextMasterPassword.text.toString()
                if (enteredText.isBlank()) {
                    dialogInputBinding.editTextMasterPassword.error = getString(R.string.master_password_empty_error)
                    return@setOnClickListener
                }
                parentFragmentManager.setFragmentResult(REQUEST_KEY_FT, bundleOf(
                    MASTER_PASSWORD_RESPONSE to enteredText)
                )
                dismiss()
            }
        }

        return dialog
    }

    companion object {
        @JvmStatic private val TAG = FirstTimeDialogFragment::class.java.simpleName
        @JvmStatic private val MASTER_PASSWORD_RESPONSE = "MASTER_PASSWORD_RESPONSE"

        @JvmStatic val REQUEST_KEY_FT = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager) {
            val dialogFragment = FirstTimeDialogFragment()
            dialogFragment.show(manager, TAG)
        }

        fun setUpListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (String) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY_FT, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getString(MASTER_PASSWORD_RESPONSE)!!)
            })
        }
    }
}