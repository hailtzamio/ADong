package com.zamio.adong.ui.project.tab.ui.main.file

import FileAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.model.FileProject
import com.zamio.adong.model.Project
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.utils.Utils
import kotlinx.android.synthetic.main.activity_stock_list.*
import kotlinx.android.synthetic.main.item_header_layout.*


class ChildFileListActivity : BaseActivity() {

    var id = 0
    private var itemList = ArrayList<FileProject>()
    private var dirPath: String? = null
    var downloadIdOne = 0
    var isDownloading = false
    override fun getLayout(): Int {
        return R.layout.activity_stock_list
    }

    override fun initView() {
        tvTitle.text = "Chi Tiết"
        rightButton.visibility = View.GONE

        progressBarOne.isIndeterminate = true
        progressBarOne.indeterminateDrawable.setColorFilter(
            Color.RED, PorterDuff.Mode.SRC_IN
        )

    }

    override fun initData() {

        dirPath = Utils.getRootDirPath(applicationContext)

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            val productOb = intent.extras!!.get(ConstantsApp.KEY_VALUES_ID) as Project
            if (productOb.designFiles != null) {
                itemList = productOb.designFiles ?: ArrayList<FileProject>()

                if (itemList.isNotEmpty()) {
                    viewNoData.visibility = View.GONE
                    setupRecyclerVieww()
                } else {
                    viewNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun resumeData() {

    }

    object MyDrawableCompat {
        fun setColorFilter(drawable: Drawable, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    private fun tryToDownload(file: FileProject) {
        isDownloading = true
        progressBarOne.visibility = View.VISIBLE

//        MyDrawableCompat.setColorFilter(progressBarOne.background,  Color.BLUE);

        downloadIdOne = PRDownloader.download(file.downloadUrl, dirPath, file.fileName)
            .build()
            .setOnStartOrResumeListener {
                progressBarOne.isIndeterminate = false
            }
//            .setOnPauseListener { buttonOne.setText(R.string.resume) }
            .setOnCancelListener {

                downloadIdOne = 0

            }
            .setOnProgressListener { progress ->
                val progressPercent =
                    progress.currentBytes * 100 / progress.totalBytes
                progressBarOne.progress = progressPercent.toInt()
//                textViewProgressOne.setText(
//                    Utils.getProgressDisplayLine(
//                        progress.currentBytes,
//                        progress.totalBytes
//                    )
//                )
                Log.d("hailpt", " " + progress.currentBytes)
                progressBarOne.isIndeterminate = false
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    showToast("Đã tải xong")
                    isDownloading = false
                    progressBarOne.visibility = View.GONE
                }

                override fun onError(error: Error) {
                    isDownloading = false
                    progressBarOne.visibility = View.GONE
                    Toast.makeText(
                        applicationContext,
                        "Có lỗi",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
    }

    private fun setupRecyclerVieww() {
        if (recyclerView != null) {
            val mAdapter = FileAdapter(itemList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->
                if (!isDownloading) {
                    val dialogClickListener =
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    tryToDownload(product)
                                }

                                DialogInterface.BUTTON_NEGATIVE -> {
                                }
                            }
                        }

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("Tải file ?")
                        .setPositiveButton("Đồng ý", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show()
                }
            }

        }
    }
}
