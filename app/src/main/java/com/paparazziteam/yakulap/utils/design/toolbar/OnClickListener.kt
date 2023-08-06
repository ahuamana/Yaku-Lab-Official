package com.paparazziteam.yakulap.utils.design.toolbar

interface OnClickListener {
    fun descargarFoto()
    fun compartirFoto()
    fun showSnackBar(message: String)
    fun requestPermission()
}