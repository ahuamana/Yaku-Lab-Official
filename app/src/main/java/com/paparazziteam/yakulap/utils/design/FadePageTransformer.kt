package com.paparazziteam.yakulap.utils.design

import android.view.View

class FadePageTransfomer : androidx.viewpager.widget.ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        if(position <= -1.0F || position >= 1.0F) {
            view.alpha = 0.0F
        } else if( position == 0.0F ) {
            view.alpha = 1.0F
        } else {
            view.alpha = 1.0F - (Math.abs(position) * 2);
        }
    }
}