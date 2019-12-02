package com.seanghay.outstagram.fragment.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.seanghay.outstagram.R
import com.seanghay.outstagram.egl.concurrency.EglDispatchQueue

class EditorFragment : Fragment() {

    private val path: String by lazy { arguments?.getString("path")!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher
            .addCallback {
                this.isEnabled = false
                showAskDialog()
            }

    }


    private fun showAskDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.discard_editing)
            .setMessage(R.string.message_discard_editing)
            .setNegativeButton(android.R.string.ok) { dialog, which ->
                findNavController().popBackStack()
                dialog.dismiss()
            }
            .setPositiveButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            .setOnCancelListener {
                requireActivity().onBackPressedDispatcher
                    .addCallback {
                        this.isEnabled = false
                        showAskDialog()
                    }
            }
            .create()
            .show()

    }

}