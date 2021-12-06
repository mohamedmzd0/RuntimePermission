package com.app.runtimepermission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


class BlankFragment : Fragment(), (PermissionCallback) -> Unit {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    private val permissionHandler = PermissionHandler(fragment = this, onResultReceived = this)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!permissionHandler.isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionHandler.requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun toast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    override fun invoke(p1: PermissionCallback) {
        when (p1) {
            PermissionCallback.DENIED -> toast("user denied")
            PermissionCallback.GENERATED -> toast("generated")
            PermissionCallback.RATIONAL -> {
                permissionHandler.showRationale(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "this message explain why i need this permission"
                )
            }
        }
    }
}