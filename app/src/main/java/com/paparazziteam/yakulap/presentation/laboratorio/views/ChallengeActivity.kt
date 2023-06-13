package com.paparazziteam.yakulap.presentation.laboratorio.views

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityChallengeBinding
import com.paparazziteam.yakulap.helper.design.toolbar.ToolbarActivity
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.getRealPathFromURI
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.presentation.laboratorio.fragments.ChallengeFragment
import com.paparazziteam.yakulap.presentation.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelLab
import com.paparazziteam.yakulap.root.ctx
import dagger.hilt.android.AndroidEntryPoint
import io.ak1.pix.helpers.PixBus
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import java.io.File


@AndroidEntryPoint
class ChallengeActivity : ToolbarActivity() {

    var binding: ActivityChallengeBinding?= null
    val TAG = javaClass.name
    var PHOTO_PATH = Uri.EMPTY
    var PHOTO_FILE:File?= null
    var ARG_JSON = "extra"
    var dataChallengeReceived = DataChallenge()
    var myToolbar: Toolbar?= null

    val mOptions = Options().apply {
        count = 1 //Number of images to restict selection count
        spanCount = 4 //Span count for gallery min 1 & max 5
        mode = Mode.Picture //Option to select only pictures or videos or both
        isFrontFacing = false
        flash = Flash.Auto
        path = "Pix/Camera"
    }

    private val resultsFragment = ChallengeFragment {
        showCameraFragment()
    }

    fun showCameraFragment() {
        addPixToActivity(R.id.container_parent_challenge, mOptions) {
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    showResultsFragment()
                    it.data.forEach { image ->
                        Log.e(TAG, "showCameraFragment: ${image}")
                        PHOTO_FILE = File(getRealPathFromURI(ctx,image))
                        PHOTO_PATH = image
                    }
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    fun getPathResultPhoto():Uri{
        return PHOTO_PATH
    }

    fun getFileResultPhoto():File?{
        return PHOTO_FILE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setColorToStatusBar(this)

        binding?.apply {
            myToolbar           = include.toolbar
        }
        extras()
        setupActionBar()
        //addFragmentFirst()
        showResultsFragment()
    }

    private fun showResultsFragment() {
        showStatusBarActivity()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_parent_challenge, resultsFragment).commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
    }

    private fun setupActionBar() {
        myToolbar?.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryYaku))
            setNavigationOnClickListener { onBackPressed() }
            title = dataChallengeReceived.tittle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun extras() {
        if (intent.extras != null) {
           var json_object =  intent.getStringExtra(ARG_JSON)
            dataChallengeReceived = fromJson(json_object?:"")
            Log.d(TAG, "Json received: $dataChallengeReceived")
        }
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container_parent_challenge)
        if (f is ChallengeFragment)
            super.onBackPressed()
        else
            PixBus.onBackPressedEvent()
    }

    fun showStatusBarActivity(){
        if (Build.VERSION.SDK_INT < 16) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = window.decorView
            // Show Status Bar.
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView.systemUiVisibility = uiOptions
        }
    }
}