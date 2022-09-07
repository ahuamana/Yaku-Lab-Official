package com.paparazziteam.yakulap.modulos.laboratorio.views

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityChallengeBinding
import com.paparazziteam.yakulap.helper.design.toolbar.ToolbarActivity
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.modulos.laboratorio.fragments.ChallengeFragment
import com.paparazziteam.yakulap.modulos.laboratorio.fragments.ChallengeFragment.Companion.TAG_CHALLENGE_FRAGMENT
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab
import io.ak1.pix.PixFragment
import io.ak1.pix.helpers.PixBus
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options

class ChallengeActivity : ToolbarActivity() {

    var binding: ActivityChallengeBinding?= null
    val TAG = javaClass.name
    var ARG_JSON = "extra"
    var dataChallengeReceived = DataChallenge()
    var myToolbar: Toolbar?= null

    //Fragments
    var TAG_CHALLENGE = "FRAGMENT_CHALLENGE"
    var TAG_PIX = "FRAGMENT_PIX"
    var fragmentActive: Fragment?= null

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
        addFragmentFirst()
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

    private fun addFragmentFirst() {
        replaceFragmentToActivity(ChallengeFragment(), TAG_CHALLENGE)
    }

    fun replaceFragmentToActivity(fragment: Fragment?, tag:String){
        if (fragment == null) return
        val manger = supportFragmentManager
        val trasaccion = manger.beginTransaction()
        trasaccion.replace(R.id.container_parent_challenge, fragment,tag)
        trasaccion.addToBackStack(null)
        trasaccion.commit()
        fragmentActive = fragment
        println("Trasaccion completed")
    }

    fun openCameraActivity(){
        //ImagePicker
        val mOptions = Options().apply {
            count = 1 //Number of images to restict selection count
            spanCount = 4 //Span count for gallery min 1 & max 5
            mode = Mode.Picture //Option to select only pictures or videos or both
            isFrontFacing = false
            flash = Flash.Auto
            path = "Pix/Camera"
        }

        addPixToActivity(R.id.container_parent_challenge, mOptions) {
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    it.data.forEach {
                        Log.e(TAG, "showCameraFragment: ${it.path}")
                    }
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                    //supportFragmentManager.popBackStack()
                }
            }

        }
        binding?.include?.root?.visibility = View.GONE
        fragmentActive = PixFragment()
    }

    private fun extras() {
        if (intent.extras != null) {
           var json_object =  intent.getStringExtra(ARG_JSON)
            dataChallengeReceived = fromJson(json_object?:"")
            Log.d(TAG, "Json received: $dataChallengeReceived")
        }
    }

    override fun onBackPressed() {
        println("onBackPressed: ${fragmentActive?.javaClass?.name}")
        if (fragmentActive?.javaClass?.name.equals(PixFragment().javaClass.name)) {
            binding?.include?.root?.visibility = View.VISIBLE
            replaceFragmentToActivity(ChallengeFragment(),TAG_CHALLENGE)
        }else finish()

    }


}