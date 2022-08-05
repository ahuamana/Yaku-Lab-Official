package com.paparazziteam.yakulap.helper.design.toolbar

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class ToolbarActivity : AppCompatActivity(), IToolbarActivity {

    override fun toolbarToLoad(toolbar: Toolbar?) {
        toolbar?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(value)
    }
}