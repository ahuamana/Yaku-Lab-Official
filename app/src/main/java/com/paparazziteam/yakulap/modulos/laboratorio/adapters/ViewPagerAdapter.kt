package com.paparazziteam.yakulap.modulos.laboratorio.adapters

import android.text.Spannable
import android.text.SpannableString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(supportFragmentPagerAdapter: FragmentManager): FragmentPagerAdapter(supportFragmentPagerAdapter) {

    var fragments = mutableListOf<Fragment>()
    var titlesFragments = mutableListOf<String>()

    fun addFragment(fragment:Fragment,title:String){
        fragments.add(fragment)
        titlesFragments.add(title)
    }

    override fun getCount(): Int  = fragments.size

    override fun getItem(position: Int): Fragment  = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        //var spannable = SpannableString("       ${titlesFragments[position]}")
        var spannable = SpannableString("${titlesFragments[position]}")
        return spannable
    }

}