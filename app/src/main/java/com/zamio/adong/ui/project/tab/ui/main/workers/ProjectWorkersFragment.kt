package com.zamio.adong.ui.project.tab.ui.main.workers

import RestClient
import WorkerCheckinOutAdapter
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserRoles
import com.google.gson.JsonElement
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.CheckinOut
import com.zamio.adong.model.Worker
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.CheckInOutDialog
import com.zamio.adong.ui.project.tab.ProjectTabActivity
import kotlinx.android.synthetic.main.fragment_main_worker.recyclerView
import kotlinx.android.synthetic.main.fragment_project_worker_checkin.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainWorkerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectWorkersFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var data = ArrayList<Worker>()
    var curentWorker: Worker? = null
    var projectId = 0
    var status = ""
    var thumbnailExtId = ""
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

        return inflater.inflate(R.layout.fragment_project_worker_checkin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectId = (activity as ProjectTabActivity).getProjectId()
        status = (activity as ProjectTabActivity).getProjectStatus()
        resetData()
        setupRecyclerView()
        getData(0)

    }

    fun pickImageFromAlbum() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(activity!!, this)
    }

    override fun onResume() {
        super.onResume()
    }

    fun resetData() {
        data.clear()
    }

    fun getData(pPage: Int) {

        data.clear()
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .getProjectWorkers((activity as ProjectTabActivity).getProjectId(), pPage)
            .enqueue(object :
                Callback<RestData<List<Worker>>> {
                override fun onFailure(call: Call<RestData<List<Worker>>>?, t: Throwable?) {
                dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<List<Worker>>>?,
                    response: Response<RestData<List<Worker>>>?
                ) {
                dismisProgressDialog()
                    if (response!!.body() != null && response.body().status == 1) {
                        data.addAll(response.body().data!!)

                        if (data.isNotEmpty()) {
                            if (viewNoData != null) {
                                viewNoData.visibility = View.GONE
                            }
                            setupRecyclerView()
                        } else {
                            if (viewNoData != null) {
                                viewNoData.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })
    }


    private fun setupRecyclerView() {
        val mAdapter = WorkerCheckinOutAdapter(data, status)
        val layoutManager = LinearLayoutManager(context)
        if (recyclerView != null) {
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = mAdapter

            mAdapter.onItemClick = { product ->

                if (ConstantsApp.USER_ROLES.contains(UserRoles.TeamLeader.type)) {

                    curentWorker = product
                    var note = ""
                    note = if (product.workingStatus == "idle") {
                        "Điểm danh công nhân vào?"
                    } else {
                        "Điểm danh công nhân ra?"
                    }

                    val dialog = CheckInOutDialog(context!!)
                    dialog.show()
                    dialog.onItemClick = {

                        val ids = ArrayList<Int>()
                        ids.add(product.id)
                        val check = CheckinOut(projectId, ids, plannedStartDate, plannedStartDate)
                        when (it) {
                            1 -> showDateTimePicker()
                            2 -> if (product.workingStatus == "idle") {
                                checkin(check)
                            } else {
                                checkout(check)
                            }
                        }
                    }

                }
            }
        }
    }

    private lateinit var date: Calendar
    var plannedStartDate = ""
    var plannedEndDate = ""
    private fun showDateTimePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            context!!,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    context!!,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)

                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

                        val formatToShow =
                            SimpleDateFormat("hh:mm a dd/MM/yyyy")

                        val dateTime = format.format(date.time).toString()
                        val dateTimeToShow = formatToShow.format(date.time).toString()
                        plannedStartDate = dateTime
                        if (curentWorker != null) {
                            val ids = ArrayList<Int>()
                            ids.add(curentWorker!!.id)
                            val check =
                                CheckinOut(projectId, ids, plannedStartDate, plannedStartDate)
                            if (curentWorker!!.workingStatus == "idle") {
                                checkin(check)
                            } else {
                                checkout(check)
                            }
                        }
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()
    }

    private fun checkout(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkout(checkinOut).enqueue(object :
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
                    Toast.makeText(context, "Chấm giờ ra thành công", Toast.LENGTH_SHORT).show()
                    getData(0)
                } else {
                    if (response!!.errorBody() != null) {
                        val obj = JSONObject(response!!.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun checkin(checkinOut: CheckinOut) {
        showProgessDialog()
        RestClient().getInstance().getRestService().checkin(checkinOut).enqueue(object :
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
                    Toast.makeText(context, "Chấm giờ vào thành công", Toast.LENGTH_SHORT).show()
                    getData(0)
                } else {
                    if (response!!.errorBody() != null) {
                        val obj = JSONObject(response!!.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    private fun uploadImage(file: File) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        showProgessDialog()
        RestClient().getRestService().updateImageCheckin(projectId, body).enqueue(object :
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
                    showToast("Thành công")

                } else {
                    if (response!!.errorBody() != null) {
//                        val obj = JSONObject(response.errorBody().string())
                        showToast("Không thành công")
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data1: Intent?) {
        super.onActivityResult(requestCode, resultCode, data1)
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

        if (resultCode == 101) {
            data.clear()
            getData(0)
        }
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
            ProjectWorkersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
