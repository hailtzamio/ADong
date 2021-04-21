package com.zamio.adong.ui.activity

import RestClient
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_preview_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.drawable.Drawable

import android.widget.Toast

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso.LoadedFrom
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.ref.WeakReference
import android.content.Intent
import android.net.Uri
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File.separator


class PreviewImageActivity : BaseActivity() {

    var id = 0
    var fileUri = ""
    override fun getLayout(): Int {
        return R.layout.activity_preview_image
    }

    override fun initView() {

    }

    override fun initData() {
        var avatarUrl = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as String
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_HIDE)) {
            avatarUrl =
                avatarUrl + "&accessToken=" + ConstantsApp.BASE64_AUTH_TOKEN.removeRange(0, 6)
        }

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_OBJECT)) {
            id = intent.extras!!.getInt(ConstantsApp.KEY_VALUES_OBJECT)
            imvRemove.visibility = View.VISIBLE
        }

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_TITLE)) {
            tvTitle.text = intent.extras!!.getString(ConstantsApp.KEY_VALUES_TITLE)
        }

        Picasso.get().load(avatarUrl).into(imvAva)

        rightButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                saveImage(Glide.with(this@PreviewImageActivity)
                    .asBitmap()
                    .load("https://i.imgur.com/4HFRb2z.jpg") // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get())
            }
//            finish()
//            DownloadAndSaveImageTask(this).execute("https://s3.amazonaws.com/appsdeveloperblog/Micky.jpg")
        }

        imvRemove.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            removeWorkOutlineCompletionPhoto()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Xóa ảnh này?").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show()
        }
    }

    override fun resumeData() {

    }

    private fun removeWorkOutlineCompletionPhoto() {

        showProgessDialog()
        RestClient().getRestService().removeWorkOutlineCompletionPhoto(id).enqueue(object :
            Callback<RestData<JsonElement>> {

            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            override fun onResponse(
                call: Call<RestData<JsonElement>>?,
                response: Response<RestData<JsonElement>>?
            ) {
                dismisProgressDialog()
                if (response!!.body() != null && response.body().status == 1) {
                    showToast("Xóa thành công")
                    setResult(100)
                    finish()
                } else {
                    showToast("Không thành công")
                }
            }
        })
    }
//
//    fun SaveImage(url: String?) {
//        Picasso.with(applicationContext).load(url).into(object : Target() {
//            fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom?) {
//                try {
//                    val mydir =
//                        File(Environment.getExternalStorageDirectory().toString() + "/11zon")
//                    if (!mydir.exists()) {
//                        mydir.mkdirs()
//                    }
//                    fileUri = mydir.absolutePath + File.separator + System.currentTimeMillis()
//                        .toString() + ".jpg"
//                    val outputStream = FileOutputStream(fileUri)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                    outputStream.flush()
//                    outputStream.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                Toast.makeText(applicationContext, "Image Downloaded", Toast.LENGTH_LONG).show()
//            }
//
//            fun onBitmapFailed(errorDrawable: Drawable?) {}
//            fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
//        })
//    }

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
        val folder = File(
            Environment.getDataDirectory().toString() + separator.toString() + "ADong"
        )
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/ADong"
        )



        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
        }
    }

    class DownloadAndSaveImageTask(context: Context) : AsyncTask<String, Unit, Unit>() {
        private var mContext: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg params: String?) {
            val url = params[0]
            val requestOptions = RequestOptions().override(100)
                .downsample(DownsampleStrategy.CENTER_INSIDE)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

            mContext.get()?.let {
                val bitmap = Glide.with(it)
                    .asBitmap()
                    .load(url)
                    .apply(requestOptions)
                    .submit()
                    .get()

                try {

                    val mydir =
                        File(Environment.getExternalStorageDirectory().toString() + "/ADong")
                    if (!mydir.exists()) {
                        mydir.mkdirs()
                    }

                    var file = it.getDir("ADong", Context.MODE_PRIVATE)
                    file = File(file, "img.jpg")
                    val out = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                    out.flush()
                    out.close()
                    Log.i("Seiggailion", "Image saved.")
                } catch (e: Exception) {
                    Log.i("Seiggailion", "Failed to save image.")
                }
            }

        }
    }
}
