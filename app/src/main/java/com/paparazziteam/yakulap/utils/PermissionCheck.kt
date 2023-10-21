package com.paparazziteam.yakulap.utils

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.paparazziteam.yakulap.root.ctx


object PermissionCheck {
    fun readExternalStorage(context: Context= ctx):Boolean {
        return if (ActivityCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(READ_EXTERNAL_STORAGE), 1)
            false
        } else {
            true
        }
    }
    fun writeExternalStorage(context: Context = ctx): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(WRITE_EXTERNAL_STORAGE), 2)
            false
        } else {
            true
        }
    }

    fun cameraCapture(context: Context): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CAMERA), 6)
            false
        } else {
            true
        }

    }

    fun audioRecord(context: Context): Boolean {

        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.RECORD_AUDIO), 2)
            false
        } else {
            true
        }

    }

    fun readAndWriteContacts(context: Context): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS), 3)
            false
        } else {
            true
        }

    }

    fun vibrate(context: Context): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.VIBRATE), 4)
            false
        } else {
            true
        }

    }

    fun sendSms(context: Context): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.SEND_SMS), 5)
            false
        } else {
            true
        }

    }
}



