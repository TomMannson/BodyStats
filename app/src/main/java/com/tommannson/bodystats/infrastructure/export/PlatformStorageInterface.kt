package com.tommannson.bodystats.infrastructure.export

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver

class PlatformStorageInterface(val activity: AppCompatActivity) : LifecycleObserver {

    var callBack: ((Uri?) -> Unit)? = null
    val saveContent = activity.registerForActivityResult(ActivityResultContracts.CreateDocument(), {
        callBack?.invoke(it)
    })

    val getContent = activity.registerForActivityResult(ActivityResultContracts.GetContent(), {
        callBack?.invoke(it)
    })


    fun saveDocumentPicker(onResult: (Uri?) -> Unit) {
        callBack = onResult
        saveContent.launch("export.csv")
    }

    fun openDocumentPicker(onResult: (Uri?) -> Unit) {
        callBack = onResult
        getContent.launch("*/*")
    }

}