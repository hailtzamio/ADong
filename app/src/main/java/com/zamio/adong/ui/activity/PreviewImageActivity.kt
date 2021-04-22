package com.zamio.adong.ui.activity

import RestClient
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.activity_preview_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*


class PreviewImageActivity : BaseActivity() {

    var id = 0
    var fileUri = ""
    var title = ""
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
            title = intent.extras!!.getString(ConstantsApp.KEY_VALUES_TITLE) ?: ""
            tvTitle.text = title
        }

        Picasso.get().load(avatarUrl).into(imvAva)

        rightButton.setOnClickListener {
            showToast("Thành công")
            CoroutineScope(Dispatchers.IO).launch {
                storeImage(Glide.with(this@PreviewImageActivity)
                    .asBitmap()
                    .load(avatarUrl) // sample image
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

    private fun storeImage(image: Bitmap) {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            image,
            title,
            "Image of $title"
        )

        Uri.parse(savedImageURL)
    }

    private fun saveImage(image: Bitmap): String? {

        var savedImagePath: String? = null
        val time = System.currentTimeMillis()
        val sdf = SimpleDateFormat("ddMyyyy")
        val currentDate = sdf.format(Date())
        val imageFileName = "$title-$currentDate.jpg"
        val file = File(baseContext.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), "ADong")

        var success = true
        if (!file.exists()) {
            success = file.mkdirs()
        }
        if (success) {
            val imageFile = File(file, imageFileName)
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
