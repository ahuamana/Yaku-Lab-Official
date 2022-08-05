package com.paparazziteam.yakulap.helper.design.toolbar

import androidx.appcompat.widget.Toolbar

interface IToolbarActivity {
    fun toolbarToLoad(toolbar: Toolbar?)
    fun enableHomeDisplay(value: Boolean)
}