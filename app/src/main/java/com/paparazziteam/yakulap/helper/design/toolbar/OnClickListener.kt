package com.paparazziteam.yakulap.helper.design.toolbar

interface OnClickListener {
    fun descargarFoto()
    fun compartirFoto()
    fun showSnackBar(message: String)
    fun requestPermission()
}