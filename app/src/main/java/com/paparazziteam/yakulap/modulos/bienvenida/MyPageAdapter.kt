package com.paparazziteam.yakulap.modulos.bienvenida

import androidx.fragment.app.FragmentManager

class MyPageAdapter(fm: FragmentManager, private val fragments: List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private var baseId: Long = 0
    override fun getItem(position: Int): androidx.fragment.app.Fragment = this.fragments[position]
    override fun getCount(): Int = this.fragments.size
    override fun getItemPosition(`object`: Any): Int = androidx.fragment.app.FragmentStatePagerAdapter.POSITION_NONE
}