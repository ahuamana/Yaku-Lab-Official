package com.paparazziteam.yakulap.presentation.bienvenida.adaters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MyPageAdapter(fm: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private var fragments: List<androidx.fragment.app.Fragment> = mutableListOf()
    fun setFragments(fragments: List<Fragment>){
        this.fragments = fragments
        notifyDataSetChanged()
    }
    override fun getItem(position: Int): Fragment = this.fragments[position]
    override fun getCount(): Int = this.fragments.size
    override fun getItemPosition(`object`: Any): Int = androidx.fragment.app.FragmentStatePagerAdapter.POSITION_NONE
}