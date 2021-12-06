package com.app.runtimepermission

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), (PermissionCallback) -> Unit {

    private val permissionHandler = PermissionHandler(activity = this, onResultReceived = this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        if (!permissionHandler.isPermissionGranted(android.Manifest.permission.RECORD_AUDIO))
//            permissionHandler.requestPermission(android.Manifest.permission.RECORD_AUDIO)
        supportFragmentManager.beginTransaction().replace(R.id.container, BlankFragment(), "tag")
            .commit()
    }

    private fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun invoke(p1: PermissionCallback) {
        when (p1) {
            PermissionCallback.DENIED -> toast("user denied")
            PermissionCallback.GENERATED -> toast("generated")
            PermissionCallback.RATIONAL -> {
                permissionHandler.showRationale(
                    android.Manifest.permission.RECORD_AUDIO,
                    "this message explain why i need this permission"
                )
            }
        }
    }


}