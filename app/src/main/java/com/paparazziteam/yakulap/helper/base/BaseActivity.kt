package com.paparazziteam.yakulap.helper.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding>(private val bindingFactory: (LayoutInflater) -> T) : AppCompatActivity() {
    protected val binding: T by lazy(LazyThreadSafetyMode.NONE) { bindingFactory(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}