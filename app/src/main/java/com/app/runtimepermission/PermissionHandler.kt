package com.app.runtimepermission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PermissionHandler(
    private val activity: AppCompatActivity? = null, private val fragment: Fragment? = null,
    private val onResultReceived: (PermissionCallback) -> Unit
) {
    // save permission to re-request it if needed
    private var permission: String? = null

    // permission launcher callback , after user request
    private var requestPermissionLauncher: ActivityResultLauncher<String>? =
        (activity?:fragment)?.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            onResultReceived(
                // user allowed
                if (granted)
                    PermissionCallback.GENERATED
                // user refused at this time
                else if (!permission.isNullOrEmpty() && (activity ?: fragment?.activity)?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            permission!!
                        )
                    } == true
                ) {
                    PermissionCallback.RATIONAL
                } else
                // user refused permission permanently
                    PermissionCallback.DENIED
            )
        }

    fun requestPermission(
        permission: String
    ) {
        this.permission = permission
        when {
            isPermissionGranted(permission) -> {
                onResultReceived(PermissionCallback.GENERATED)
            }
            isPermissionRationale(permission) -> {
                // user refused permission before but you can request it again
                onResultReceived(PermissionCallback.RATIONAL)
            }
            else -> requestPermissionLauncher?.launch(permission)
        }
    }

    fun showRationale(permission: String, message: String) {
        (activity ?: fragment?.context)?.let {
            MaterialAlertDialogBuilder(it).setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Allow") { dialog, which ->
                    requestPermissionLauncher?.launch(permission)
                    dialog.dismiss()
                }.show()
        }
    }

    fun isPermissionGranted(permission: String): Boolean {
        return (activity ?: fragment?.context)?.let {
            ContextCompat.checkSelfPermission(
                it,
                permission
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionRationale(permission: String): Boolean {
        return (activity ?: fragment?.activity)?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                permission
            )
        } == true
    }
}


sealed class PermissionCallback {
    // show rationale
    object RATIONAL : PermissionCallback()

    // refused
    object DENIED : PermissionCallback()

    //allowed
    object GENERATED : PermissionCallback()
}
