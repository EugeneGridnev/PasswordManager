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

class MasterPasswordDialogFragment : DialogFragment() {

    private val masterPassword: String?
        get() = requireArguments().getString(ARG_MASTER_PASSWORD)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogInputBinding = MasterPasswordEditTextBinding.inflate(layoutInflater)

        val dialog =  AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.enter_master_password_title)
            .setView(dialogInputBinding.root)
            .setPositiveButton(R.string.ok_dialog_button_text, null)
            .setNegativeButton(R.string.cancel_dialog_button_text, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogInputBinding.editTextMasterPassword.text.toString()
                if (masterPassword == enteredText) {

                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY, bundleOf(
                            MASTER_PASSWORD_RESPONSE to true)
                    )
                    dismiss()
                } else {
                    dialogInputBinding.editTextMasterPassword.error = getString(R.string.master_password_wrong_error)
                    return@setOnClickListener
                }
            }
        }

        return dialog
    }

    companion object {
        @JvmStatic private val TAG = MasterPasswordDialogFragment::class.java.simpleName
        @JvmStatic private val MASTER_PASSWORD_RESPONSE = "MASTER_PASSWORD_RESPONSE"
        @JvmStatic private val ARG_MASTER_PASSWORD = "ARG_MASTER_PASSWORD"

        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, masterPassword:String) {
            val dialogFragment = MasterPasswordDialogFragment()
            dialogFragment.arguments = bundleOf( ARG_MASTER_PASSWORD to masterPassword)
            dialogFragment.show(manager, TAG)
        }

        fun setUpListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Boolean) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getBoolean(MASTER_PASSWORD_RESPONSE))
            })
        }
    }
}