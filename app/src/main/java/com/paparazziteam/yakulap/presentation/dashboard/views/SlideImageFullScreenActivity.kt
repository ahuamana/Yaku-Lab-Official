package com.paparazziteam.yakulap.presentation.dashboard.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.utils.convertStringToList
import com.paparazziteam.yakulab.binding.utils.setColorToStatusBar
import com.paparazziteam.yakulab.binding.utils.downloadImage
import com.paparazziteam.yakulab.binding.utils.getUriFromBitmap
import com.paparazziteam.yakulap.utils.design.toolbar.DescargaImagenes
import com.paparazziteam.yakulap.utils.design.toolbar.OnClickListener
import com.paparazziteam.yakulap.utils.design.toolbar.ToolbarActivity
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.presentation.bienvenida.adaters.PageAdaper
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.SlideImageViewModel
import com.paparazziteam.yakulap.root.ctx
import com.yakulab.usecases.inaturalist.ActionFile
import com.yakulab.usecases.inaturalist.DownloadFileResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
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

    val viewModel by viewModels<SlideImageViewModel>()

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
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.eventsDownloadFile.collect() {
                    handleEventsDownloadFile(it)
                }
            }
        }

    }

    private fun handleEventsDownloadFile(it: DownloadFileResult) {
        when(it){
            is DownloadFileResult.Failure -> {
                showSnackBar(it.error)
            }
            DownloadFileResult.HideLoading -> {/*TODO*/ }
            DownloadFileResult.Idle -> {/*TODO*/}
            DownloadFileResult.ShowLoading -> {/*TODO*/ }
            is DownloadFileResult.SuccessDownload -> it.run{
                handleDownloadFileImage(response)
            }
            is DownloadFileResult.SuccessShare -> it.run{
                val imageUri = getUriFromBitmap(this@SlideImageFullScreenActivity,response)
                handleShareFileImage(imageUri)
            }
        }
    }

    private fun handleShareFileImage(imageUri: Uri) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir comprobante con..."))
    }

    fun handleDownloadFileImage(response: Bitmap) {
        val imageUri = getUriFromBitmap(this@SlideImageFullScreenActivity,response)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(imageUri, "image/jpeg")
        startActivity(intent)
    }

    fun bottomsheet(){
        btnSheet        = findViewById(com.paparazziteam.yakulap.R.id.bottomsheet_opciones)
        optionCompartir = findViewById(R.id.opcion_compartir)
        optionDescargar = findViewById(R.id.opcion_descargar)
        optionCompartir.setOnClickListener {
            listener.compartirFoto()
            bottomSheetTerminosYCondicones.state = BottomSheetBehavior.STATE_HIDDEN
        }
        optionDescargar.setOnClickListener {
            listener.descargarFoto()
            bottomSheetTerminosYCondicones.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun descargarFoto() {
        val url = fotos[mPosition]
        val fileName = url.substring(url.lastIndexOf('/') + 1)

        if(url.contains("firebase", true)){
            lifecycleScope.launch {
                val newUrl = url.substring(url.indexOf("com/") + 4)
                viewModel.downloadFile(newUrl, ActionFile.DOWNLOAD)
            }
        }else{
            Timber.d("Es una imagen de internet")
            lifecycleScope.launch {
                downloadImage(this@SlideImageFullScreenActivity,url, fileName)
            }
        }
    }

    override fun compartirFoto() {
        val url = fotos[mPosition]
        val fileName = url.substring(url.lastIndexOf('/') + 1)
        if(url.contains("firebase", true)){
            //get url without base url -> https://firebasestorage.googleapis.com/
            val newUrl = url.substring(url.indexOf("com/") + 4)
            lifecycleScope.launch {
                viewModel.downloadFile(newUrl, ActionFile.SHARE)
            }
        }else{
            Timber.d("Es una imagen de internet")
            lifecycleScope.launch {
                viewModel.downloadFile(url, ActionFile.SHARE)
            }
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



