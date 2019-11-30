package com.seanghay.outstagram.utility

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.seanghay.outstagram.R


object PermissionUtility {
    const val READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE
    const val WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun requestReadWrite(
        fragment: Fragment,
        requestCode: Int = AndroidUtility.RC_READ_WRITE_EXTERNAL_STORAGE
    ) {
        fragment.requestPermissions(arrayOf(READ_EXTERNAL, WRITE_EXTERNAL), requestCode)
    }

    fun isReadWriteGranted(fragment: Fragment): Boolean {
        return arrayOf(READ_EXTERNAL, WRITE_EXTERNAL).all { isGranted(fragment, it) }
    }

    fun isGranted(fragment: Fragment, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isRationale(fragment: Fragment, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            fragment.requireActivity(),
            permission
        )
    }

    fun showApplicationSettings(
        fragment: Fragment,
        requestCode: Int = AndroidUtility.RC_RATIONALE
    ) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", fragment.requireContext().packageName, null)
        intent.data = uri
        fragment.startActivityForResult(intent, requestCode)
    }

    fun showPermissionRequiredDialog(fragment: Fragment) {
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(R.string.storage_permission)
            .setMessage(R.string.message_storage_permission)
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                showApplicationSettings(fragment)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.cancel()
            }
            .setOnCancelListener {
                fragment.activity?.finish()
            }
            .create()
            .show()
    }
}