package ru.eugeneprojects.passwordmanager.views.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import ru.eugeneprojects.passwordmanager.R
import ru.eugeneprojects.passwordmanager.databinding.MasterPasswordEditTextBinding

class ChangeMasterPasswordDialogFragment : DialogFragment() {

    private val masterPassword: String?
        get() = requireArguments().getString(ARG_MASTER_PASSWORD_CMP)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogInputBinding = MasterPasswordEditTextBinding.inflate(layoutInflater)

        val dialog =  AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.change_master_password_title)
            .setView(dialogInputBinding.root)
            .setPositiveButton(R.string.ok_dialog_button_text, null)
            .setNegativeButton(R.string.cancel_dialog_button_text, null)
            .create()

        dialogInputBinding.editTextMasterPassword.setText(masterPassword)

        dialogInputBinding.editTextMasterPassword.addTextChangedListener { text ->
            if (text?.isBlank() == false) {
                dialogInputBinding.textInputMasterPassword.helperText = ""
            }
        }

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogInputBinding.editTextMasterPassword.text.toString()
                if (enteredText.isBlank()) {
                    dialogInputBinding.textInputMasterPassword.helperText = getString(R.string.master_password_empty_error)
                    return@setOnClickListener
                }

                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY_CMP, bundleOf(
                    MASTER_PASSWORD_RESPONSE_CMP to enteredText)
                )
                dismiss()
            }
        }

        return dialog
    }

    companion object {
        @JvmStatic private val TAG = ChangeMasterPasswordDialogFragment::class.java.simpleName
        @JvmStatic private val MASTER_PASSWORD_RESPONSE_CMP = "MASTER_PASSWORD_RESPONSE"
        @JvmStatic private val ARG_MASTER_PASSWORD_CMP = "ARG_MASTER_PASSWORD"

        @JvmStatic val REQUEST_KEY_CMP = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, masterPassword:String) {
            val dialogFragment = ChangeMasterPasswordDialogFragment()
            dialogFragment.arguments = bundleOf( ARG_MASTER_PASSWORD_CMP to masterPassword)
            dialogFragment.show(manager, TAG)
        }

        fun setUpListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (String) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY_CMP, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getString(MASTER_PASSWORD_RESPONSE_CMP)!!)
            })
        }
    }
}