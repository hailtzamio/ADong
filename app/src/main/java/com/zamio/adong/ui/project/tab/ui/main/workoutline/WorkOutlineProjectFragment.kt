package com.zamio.adong.ui.project.tab.ui.main.workoutline

import RestClient
import WorkOutlineProjectAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.gson.JsonElement
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Project
import com.zamio.adong.model.WorkOutline
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.PreviewImageActivity
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import com.zamio.adong.ui.project.tab.ui.main.album.FinishProjectAlbumActivity
import kotlinx.android.synthetic.main.fragment_main_workeoutline.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainWorkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainWorkOutlineFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    var currentPage = 0
    var totalPages = 0
    var products: List<WorkOutline>? = null
    var workOutlineId = 0
    var isReuploadImageWorkoutline = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_workeoutline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(0)
        getProject()
        tvOk.setOnClickListener {
            ImagePicker.create(this).multi()
                .toolbarImageTitle("Chọn tối đa 8 ảnh")
                .limit(8)
                .start()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getData(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getProjectWorkOutlines((activity as ProjectTabActivity).getProjectId(), page)
            .enqueue(object :
                Callback<RestData<List<WorkOutline>>> {
                override fun onFailure(call: Call<RestData<List<WorkOutline>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<List<WorkOutline>>>?,
                    response: Response<RestData<List<WorkOutline>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        products = response.body().data!!
                        setupRecyclerView()

                        var isShowTvOK = true
                        if (products != null && products!!.isNotEmpty()) {
                            products!!.forEach {
                                if (it.finishDatetime == null) {
                                    isShowTvOK = false
                                }
                            }

                            if (isShowTvOK && tvOk != null) {
                                tvOk.visibility = View.VISIBLE
                            } else {
                                tvOk.visibility = View.GONE
                            }
                        }


                    }
                }
            })
    }

    private fun setupRecyclerView() {
        if (recyclerView != null) {
            val mAdapter = WorkOutlineProjectAdapter(products!!)
            val linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->

                if (product.finishDatetime == null || (product.photos != null && product.photos.isEmpty())) {

                    var title = "Hạng mục đã hoàn thành?"
                    if(product.finishDatetime != null) {
                        title = "Tải ảnh?"
                        isReuploadImageWorkoutline = true
                    }

                    if (ConstantsApp.USER_ROLES.contains(UserRoles.TeamLeader.type) || ConstantsApp.USER_ROLES.contains(
                            UserRoles.Contractor.type
                        ) || ConstantsApp.USER_ROLES.contains(UserRoles.Admin.type)
                    ) {
                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        workOutlineId = product.id
                                        pickImageFromAlbum()
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                    }
                                }
                            }

                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder.setMessage(title)
                            .setPositiveButton("Đồng ý", dialogClickListener)
                            .setNegativeButton("Không", dialogClickListener).show()
                    }
                } else {
                    if (product.photos != null && product.photos.isNotEmpty()) {
                        val intent = Intent(context, PreviewImageActivity::class.java)
                        intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.id)
                        intent.putExtra(ConstantsApp.KEY_VALUES_TITLE, product.workOutlineName)
                        intent.putExtra(ConstantsApp.KEY_VALUES_OBJECT,product.photos[0].photoId)
                        intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.photos[0].thumbnailUrl)
                        startActivityForResult(intent, 1000)
                    }
                }

            }
        }
    }

    fun getProject() {
        RestClient().getInstance().getRestService()
            .getProject((activity as ProjectTabActivity).getProjectId()).enqueue(object :
                Callback<RestData<Project>> {

                override fun onFailure(call: Call<RestData<Project>>?, t: Throwable?) {

                }

                override fun onResponse(
                    call: Call<RestData<Project>>?,
                    response: Response<RestData<Project>>?
                ) {
                    if (response!!.body() != null && response.body().status == 1) {
                        val data = response.body().data ?: return
                        if (data.status == "DONE") {
                            tvOk.text = "ĐÃ HOÀN THÀNH"
                            tvOk.setTextColor(Color.WHITE)

                            tvOk.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.button_normal_main_radius_green_layout,
                                null
                            )

                            tvOk.setOnClickListener {
                                val intent = Intent(context, FinishProjectAlbumActivity::class.java)
                                intent.putExtra(ConstantsApp.KEY_VALUES_ID, data.id)
                                startActivityForResult(intent, 1000)
                            }
                        }
                    }
                }
            })
    }

    private fun finishWorkOutline(id: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().finishProjectWorkOutlines(id).enqueue(object :
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
                    showToast("Thành công")
                } else {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun finishProject(id: Int, images: List<Image>) {
        showProgessDialog()
        RestClient().getInstance().getRestService().finishProject(id).enqueue(object :
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
                    getProject()
                    images.forEach {
                        uploadProjectCompletion(File(it.path))
                    }
                } else {
                    if (response.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    fun pickImageFromAlbum() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(activity!!, this)
    }

    private fun uploadImage(file: File) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        showProgessDialog()
        RestClient().getRestService().finishImageProjectWorkOutlines(workOutlineId, body)
            .enqueue(object :
                Callback<RestData<JsonElement>> {

                override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<JsonElement>>?,
                    response: Response<RestData<JsonElement>>?
                ) {
                    dismisProgressDialog()
                    if (response?.body() != null && response.body().status == 1) {
                        if(!isReuploadImageWorkoutline) {
                            finishWorkOutline(workOutlineId)
                        }
                        getData(0)
                        isReuploadImageWorkoutline = false
                    } else {
                        if (response!!.errorBody() != null) {
                            val obj = JSONObject(response.errorBody().string())
                            showToast(obj["message"].toString())
                        }
                    }
                }
            })
    }


    private fun uploadProjectCompletion(file: File) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        showProgessDialog()
        RestClient().getRestService()
            .finishImageProjectConpletion((activity as ProjectTabActivity).getProjectId(), body)
            .enqueue(object :
                Callback<RestData<JsonElement>> {

                override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<JsonElement>>?,
                    response: Response<RestData<JsonElement>>?
                ) {
                    dismisProgressDialog()
                    if (response?.body() != null && response.body().status == 1) {

                    } else {
                        if (response!!.errorBody() != null) {
                            val obj = JSONObject(response.errorBody().string())
                            showToast(obj["message"].toString())
                        }
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data1: Intent?) {
        super.onActivityResult(requestCode, resultCode, data1)

        if(requestCode == 1000) {
            getData(0)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data1)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                val file = File(resultUri.path!!)
                uploadImage(file)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        if (ImagePicker.shouldHandle(requestCode, resultCode, data1)) {
            // Get a list of picked images
            val images = ImagePicker.getImages(data1)
            if (images.size > 0) {
                finishProject((activity as ProjectTabActivity).getProjectId(), images)
            }


            // or get a single image only
//            val image = ImagePicker.getFirstImageOrNull(data1)
        }

    }

    private fun getPathFromURI(uri: Uri) {
//        var path: String = uri.path // uri = any content Uri
//
//        val databaseUri: Uri
//        val selection: String?
//        val selectionArgs: Array<String>?
//        if (path.contains("/document/image:")) { // files selected from "Documents"
//            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            selection = "_id=?"
//            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
//        } else { // files selected from all other sources, especially on Samsung devices
//            databaseUri = uri
//            selection = null
//            selectionArgs = null
//        }
//        try {
//            val projection = arrayOf(
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.ORIENTATION,
//                MediaStore.Images.Media.DATE_TAKEN
//            ) // some example data you can query
//            val cursor = contentResolver.query(
//                databaseUri,
//                projection, selection, selectionArgs, null
//            )
//            if (cursor.moveToFirst()) {
//                val columnIndex = cursor.getColumnIndex(projection[0])
//                imagePath = cursor.getString(columnIndex)
//                // Log.e("path", imagePath);
//                imagesPathList.add(imagePath)
//            }
//            cursor.close()
//        } catch (e: Exception) {
//            Log.e(TAG, e.message, e)
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainWorkerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainWorkOutlineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
