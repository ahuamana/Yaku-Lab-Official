package com.paparazziteam.yakulap.presentation.bienvenida.adaters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ViewpagerImageBinding
import com.paparazziteam.yakulap.helper.application.PermissionCheck.writeExternalStorage
import com.paparazziteam.yakulap.helper.design.toolbar.DescargaImagenes
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal class PageAdaper(var mContext: Context, private val mResourceIds: List<String>) : androidx.viewpager.widget.PagerAdapter() ,
    DescargaImagenes {

// These matrices will be used to move and zoom image
    val matrix = Matrix()
    val savedMatrix = Matrix();
    var NONE = 0;
    var DRAG = 1;
    var ZOOM = 2;
    var mode = NONE;
    var start =  PointF();
    var mid =  PointF();
    var oldDist = 1f;
    lateinit var savedItemClicked :String
    private var downloadManager: DownloadManager? = null
    var imagen :ImageView? = null
    private val TAG = this::class.java.name
    private var mLayoutInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var imgComplet :Boolean? = null
    lateinit var  photoView :ImageView



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false)
        val bindingVPImage = ViewpagerImageBinding.bind(itemView)

        photoView = bindingVPImage.imageviewSlide
        imagen    = bindingVPImage.imgPrueba

        val dis = DisplayMetrics()
        val height = dis.heightPixels
        val width  = dis.widthPixels

        photoView.minimumHeight = height
        photoView.minimumWidth  = width

        try{
            Glide.with(mContext)
                .load(mResourceIds[position])
                .into(photoView)
        }catch(e:Throwable){

            Glide.with(mContext)
                .load(mResourceIds[position])
                .into(imagen!!)
        }


        container.addView(itemView)
        return itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean { return view === `object` }

    override fun getCount(): Int { return mResourceIds.size }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) { container.removeView(`object` as ConstraintLayout) }

    override fun descargar(url :String) {}

    override fun compartir(url: String) {

        imagen?.let {
            Glide.with(mContext)
                .load(url)
                .into(it)
        }
        if(writeExternalStorage(mContext)){
            compartirFot()
        }
    }
    private fun compartirFot(){
        try {
            imagen!!.buildDrawingCache();
            val bitmap = imagen!!.drawingCache;
            try {
                val cachePath = File(mContext.cacheDir, "imagenes") //path cache.
                cachePath.mkdirs() // Crea directorio si no existe.
                val stream = FileOutputStream("$cachePath/imagen.jpg") // Escribe imagen.
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

            val imagePath = File(mContext.cacheDir, "imagenes") //obtiene directorio.
            val newFile = File(imagePath, "imagen.jpg") //obtiene imagen.

            val PACKAGE_NAME =  "com.paparazziteam.yakulap.provider.profile"

            val contentUri = FileProvider.getUriForFile(mContext.applicationContext, PACKAGE_NAME, newFile)

            if (contentUri != null) {

                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, mContext.contentResolver.getType(contentUri))
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                mContext.startActivity(Intent.createChooser(shareIntent, "Elige una aplicación:"))

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}