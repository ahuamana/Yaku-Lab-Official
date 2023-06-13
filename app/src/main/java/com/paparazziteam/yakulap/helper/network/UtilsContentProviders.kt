package com.paparazziteam.yakulap.helper.network

import android.content.Context
import android.content.Intent
import android.net.Uri

//open an url using Content Providers
fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    context.startActivity(intent)
}