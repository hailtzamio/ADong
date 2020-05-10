package com.zamio.adong.ui.project.tab.ui.main.requirement

import ProductAdapter
import RestClient
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.LinesAddNew
import com.zamio.adong.model.Product
import com.zamio.adong.model.ProductRequirementRes
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.product.DetailProductActivity
import kotlinx.android.synthetic.main.activity_create_product_requirement.*
import kotlinx.android.synthetic.main.item_header_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateProductRequirementActivity : BaseActivity() {

    var currentPage = 0
    var totalPages = 0
    var workers = ArrayList<Product>()
    var workersChoose = ArrayList<Product>()
    var id = 0
    override fun getLayout(): Int {
        return R.layout.activity_create_product_requirement
    }

    override fun initView() {
        rightButton.setImageResource(R.drawable.tick)
    }

    override fun initData() {

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

        getProducts(0)
        imvBack.setOnClickListener {
            onBackPressed()
        }

        if (!ConstantsApp.PERMISSION!!.contains("c")) {
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        tvChooseDate.setOnClickListener {
            hideKeyboard()
            showDateTimePicker()
        }

        tvTitle.text = "Danh Sách Vật Tư"
        rightButton.setOnClickListener {

            for (j in (workers.size - 1) downTo 0) {
                if (workers[j].quantity != 0) {
                    workersChoose.add(workers[j])
                }
            }

            val addNew = ArrayList<LinesAddNew>()

            workersChoose.forEach {
                addNew.add(LinesAddNew(it.id,it.quantity))
            }

            val productRequirementRes = ProductRequirementRes(plannedStartDate,addNew,edtNote.text.toString())
            create(productRequirementRes)
        }
    }

    private lateinit var date: Calendar
    var plannedStartDate = ""
    private fun showDateTimePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        val format =
                            SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

                        val formatToShow =
                            SimpleDateFormat("dd/MM/yyyy hh:mm a")

                        val dateTime = format.format(date.time).toString()
                        val dateTimeToShow = formatToShow.format(date.time).toString()
                        plannedStartDate = dateTime
                        tvChooseDate.text = dateTimeToShow
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

    private fun setupSelectedCheck() {


    }

    override fun resumeData() {

    }

    override fun onResume() {
        super.onResume()

    }

    private fun create(jsonObject: ProductRequirementRes) {
        showProgessDialog()
        RestClient().getInstance().getRestService().createProductRequirementForProject(jsonObject,id).enqueue(object :
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
                    showToast("Tạo thành công")
                    setResult(101)
                    finish()
                } else {
                    val obj = JSONObject(response.errorBody().string())
                    showToast(obj["message"].toString())
                }
            }
        })
    }

    private fun getProducts(page: Int) {
        showProgessDialog()
        RestClient().getInstance().getRestService().getProducts(page, edtSearch.text.toString())
            .enqueue(object :
                Callback<RestData<ArrayList<Product>>> {
                override fun onFailure(call: Call<RestData<ArrayList<Product>>>?, t: Throwable?) {
                    dismisProgressDialog()
                }

                override fun onResponse(
                    call: Call<RestData<ArrayList<Product>>>?,
                    response: Response<RestData<ArrayList<Product>>>?
                ) {
                    dismisProgressDialog()
                    if (response!!.body().status == 1) {
                        val productRes = response.body().data!!


                        totalPages = response.body().pagination!!.totalPages!!

                        for (j in (workers.size - 1) downTo 0) {
                            if (workers[j].quantity != 0) {
                                workersChoose.add(workers[j])
                            }
                        }

                        for (i in workersChoose.indices) {
                            for (j in (productRes.size - 1) downTo 0) {
                                if (workersChoose[i].id == productRes[j].id) {
                                    productRes[j].quantity = workersChoose[i].quantity
                                }
                            }
                        }

                        workers = productRes

                        setupRecyclerView()
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        val mAdapter = ProductAdapter(workers, true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            intent.putExtra(ConstantsApp.ChooseTeamWorkerActivity, "")
            startActivityForResult(intent, 1000)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mAdapter.onItemSelected = { position, isChecked ->
            //            workers[position].isSelected = isChecked
//
//            if (isChecked) {
//                workersChoose.add(workers[position])
//            } else {
//                for (i in workersChoose.indices) {
//                    for (j in (workersChoose.size - 1) downTo 0) {
//                        if (workers[position].id == workersChoose[j].id) {
//                            workersChoose.removeAt(j)
//                        }
//                    }
//                }
//            }
        }
    }
}
