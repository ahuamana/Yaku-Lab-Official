package com.paparazziteam.yakulap.utils.design

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.paparazziteam.yakulab.binding.Constants
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.convertStringToList
import com.paparazziteam.yakulab.binding.helper.setColorToStatusBar
import com.paparazziteam.yakulap.utils.PermissionCheck.readExternalStorage
import com.paparazziteam.yakulap.utils.design.toolbar.DescargaImagenes
import com.paparazziteam.yakulap.utils.design.toolbar.OnClickListener
import com.paparazziteam.yakulap.utils.design.toolbar.ToolbarActivity
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.presentation.bienvenida.adaters.PageAdaper
import com.paparazziteam.yakulap.root.ctx
import com.paparazziteam.yakulap.utils.downloadData

class SlideImageFullScreenActivity : ToolbarActivity(), OnClickListener {

    private val TAG = this::class.java.name
    private val TITULO_IMG = "imagen"
    private val CARP_DEST  = "/Picture"
    private lateinit var toolbar  : Toolbar
    private lateinit var viewpager: androidx.viewpager.widget.ViewPager
    private lateinit var adapter  : PageAdaper
    private lateinit var presenter: DescargaImagenes
    private lateinit var btnArrow :ImageButton
    private lateinit var btnDespl :ImageButton
    private lateinit var lyFullScreem : CoordinatorLayout
    private lateinit var btnDesca2 :ImageButton
    private lateinit var cortinaBtsh                 : RelativeLayout
    private lateinit var btshListTerYCond : ConstraintLayout
    private lateinit var bottomSheetTerminosYCondicones : BottomSheetBehavior<ConstraintLayout>
    var fotos = mutableListOf<String>()
    var mPosition = 0
    lateinit var optionDescargar: ConstraintLayout
    lateinit var optionCompartir: ConstraintLayout
    lateinit var btnSheet : ConstraintLayout
    private lateinit var listener: OnClickListener

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_image_full_screen)
        toolbar          = findViewById(R.id.toolbar_slide)
        btnDespl         = findViewById(R.id.btn_desplegar)
        btnArrow         = findViewById(R.id.btn_atras)
        btnDesca2        = findViewById(R.id.btn_descarga2)
        lyFullScreem     = findViewById(R.id.layout_full_screen)
        cortinaBtsh      = findViewById(R.id.cortina_bottomsheet_cortinas_img)
        btshListTerYCond = findViewById(R.id.bottomsheet_opciones)
        bottomSheetTerminosYCondicones = BottomSheetBehavior.from(btshListTerYCond)

        listener = this
        toolbar.setBackgroundColor(R.color.black)
        btnArrow.setOnClickListener {
            onBackPressed()
        }

        toolbarToLoad(toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        intent?.extras?.let {
            viewpager = findViewById(R.id.viewpager_image)
            viewpager.scaleX
            viewpager.scaleY
            viewpager.translationX
            viewpager.translationY
            //viewpager.zoomControls()
            fotos = it.getString("lista_imagenes")?.let { it1 -> convertStringToList(it1) } as MutableList
            val mImageAdapter = PageAdaper(this, fotos)
            presenter         = mImageAdapter
            adapter           = mImageAdapter
            viewpager.adapter = adapter
            viewpager.currentItem = it.getInt("position")
        }
        viewpager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPosition = position
            }
        })

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            // versiones con android 6.0 o superior
            btnDespl.visibility = View.VISIBLE
            btnDesca2.visibility = View.GONE
            btnDespl.setOnClickListener {
                cortinaBtsh.setBackgroundColor(getResources().getColor(R.color.colorCortina))
                bottomSheetTerminosYCondicones.state  = BottomSheetBehavior.STATE_EXPANDED

//                bottonSheetBehavior = ImageOptionBottomSheet()
//                bottonSheetBehavior.show(supportFragmentManager, bottonSheetBehavior.tag)
            }

        } else{
            // para versiones anteriores a android 6.0
            btnDespl.visibility = View.GONE
            btnDesca2.visibility = View.VISIBLE
            btnDesca2.setOnClickListener { descargarFoto() }
        }
        setColorToStatusBar(this)

        cortinaBtsh.setOnClickListener{
            cortinaBtsh.setBackgroundColor(getResources().getColor(R.color.colorTransparent))
            bottomSheetTerminosYCondicones.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetTerminosYCondicones.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState >= 4) {
                    cortinaBtsh.visibility = View.GONE
                    setColorToStatusBar(this@SlideImageFullScreenActivity, MyPreferences().color)
                } else {
                    setColorToStatusBar(this@SlideImageFullScreenActivity, ContextCompat.getColor(ctx, R.color.colorCortina))
                    cortinaBtsh.visibility = View.VISIBLE
                }
            }
        })
        bottomsheet()
    }

    fun bottomsheet(){
        btnSheet        = findViewById(com.paparazziteam.yakulap.R.id.bottomsheet_opciones)
        optionCompartir = findViewById(R.id.opcion_compartir)
        optionDescargar = findViewById(R.id.opcion_descargar)
        optionCompartir.setOnClickListener {
            bottomSheetTerminosYCondicones.state = BottomSheetBehavior.STATE_HIDDEN
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                listener.requestPermission()
            } else {
                listener.compartirFoto()
            }
        }
        optionDescargar.setOnClickListener {
            bottomSheetTerminosYCondicones.state = BottomSheetBehavior.STATE_HIDDEN
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                listener.requestPermission()
            } else {
                listener.descargarFoto()
            }
        }
    }

    override fun descargarFoto() {
        if(readExternalStorage(this)){
            try {
                lyFullScreem.downloadData(fotos[mPosition], CARP_DEST,TITULO_IMG, Constants.EXT_JPG)
            } catch (e:Exception) {
                showSnackBar("No se puedo descargar la imagen.")
                Log.d("ImgManager", "Error: $e")
            }
        }
    }

    override fun compartirFoto() {
        if(readExternalStorage(this)){
            presenter.compartir(fotos[mPosition])
        }
    }

    override fun showSnackBar(message: String) {
        showSnackBar(message)
    }

    override fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
    }
}



