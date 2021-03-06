package com.zamio.adong.ui.project.tab.ui.main.requirement

import ProductAdapter
import ProductRequirement2Adapter
import RestClient
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.ui.network.UserPermission
import com.google.gson.JsonElement
import com.zamio.adong.R
import com.zamio.adong.model.LinesAddNew
import com.zamio.adong.model.Product
import com.zamio.adong.model.ProductRequirement
import com.zamio.adong.model.ProductRequirementRes
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.popup.ConfirmProductRequirementDialog
import com.zamio.adong.popup.HoldSimDialog
import com.zamio.adong.ui.product.DetailProductActivity
import com.zamio.adong.ui.trip.CreateTripActivity
import com.zamio.adong.ui.trip.TripActivity
import kotlinx.android.synthetic.main.activity_create_product_requirement.*
import kotlinx.android.synthetic.main.item_header_layout.*
import kotlinx.android.synthetic.main.item_search_layout.*
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
    var products = ArrayList<Product>()
    var productChoose = ArrayList<Product>()
    var id = 0
    var isFromUpdateProductRequirement = false
    var productRequirement: ProductRequirement? = null
    override fun getLayout(): Int {
        return R.layout.activity_create_product_requirement
    }

    override fun initView() {
        rightButton.setImageResource(R.drawable.tick)
    }

    override fun initData() {

        id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 0)

        if (intent.hasExtra(ConstantsApp.KEY_VALUES_FROM_DETAIL_PRODUCT_REQ)) {
            isFromUpdateProductRequirement = true
            productRequirement =
                intent.extras!!.get(ConstantsApp.KEY_VALUES_FROM_DETAIL_PRODUCT_REQ) as ProductRequirement
        }

        getProducts(0)
        imvBack.setOnClickListener {
            onBackPressed()
        }

        if (!ConstantsApp.USER_PERMISSIONS.contains(UserPermission.ProductRequirementc.type)) {
            rightButton.visibility = View.GONE
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProducts(0)
                return@OnEditorActionListener true
            }
            false
        })

        imvSearch.setOnClickListener {
            getProducts(0)
        }

        rlDate.setOnClickListener {
            hideKeyboard()
            showDateTimePicker()
        }

        tvTitle.text = "Thêm Vật Tư"
        rightButton.setOnClickListener {

            showConfirmPopup()

        }
    }

    private fun showConfirmPopup() {

        for (j in (products.size - 1) downTo 0) {
            if (products[j].quantityChoose != 0) {
                Log.e("hailpt~~", " Come ")
                productChoose.add(products[j])
            }
        }

        Log.e("hailpt~~", " before " + productChoose.size)

        val addNew = ArrayList<LinesAddNew>()

        var count = productChoose.size
        for (i in 0 until count) {
            var j = i + 1
            while (j < count) {
                if (productChoose[i].id == productChoose[j].id) {
                    productChoose.removeAt(j--)
                    count--
                }
                j++
            }
        }

        Log.e("hailpt~~", " after " + productChoose.size)

        productChoose.forEach {
            if (it.quantityChoose != 0) {
                addNew.add(
                    LinesAddNew(
                        null,
                        0,
                        it.id,
                        it.quantityChoose,
                        it.name ?: "---",
                        null,
                        it.unit ?: "---",
                        it.note ?: ""
                    )
                )
            }
        }

        if (isFromUpdateProductRequirement && productRequirement != null) {
            if(plannedStartDate != "") {
                productRequirement!!.expectedDatetime = plannedStartDate
            }

            if(edtNote.text.toString().trim() != "") {
                productRequirement!!.note = edtNote.text.toString().trim()
            }

            productRequirement!!.linesAddNew = addNew

            updateItemProductRequirement(productRequirement!!)
        } else {
            val dialog = ConfirmProductRequirementDialog(this, addNew)
            dialog.show()

            dialog.onItemClick = {
                if (it == 2) {
                    val productRequirementRes =
                        ProductRequirementRes(plannedStartDate, addNew, edtNote.text.toString())
                    create(productRequirementRes)
                }
            }
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

    private fun updateItemProductRequirement(productRequirement: ProductRequirement) {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .removeItemProductRequirement(productRequirement, productRequirement.id)
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
                    if (response!!.body() != null && response.body().status == 1) {
                        showToast("Thành công")
                        finish()
                    }
                }
            })
    }

    private fun create(jsonObject: ProductRequirementRes) {
        showProgessDialog()
        RestClient().getInstance().getRestService()
            .createProductRequirementForProject(jsonObject, id).enqueue(object :
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

                        for (j in (products.size - 1) downTo 0) {
                            if (products[j].quantityChoose != 0) {
                                productChoose.add(products[j])
                            }
                        }

                        for (i in productChoose.indices) {
                            for (j in (productRes.size - 1) downTo 0) {
                                if (productChoose[i].id == productRes[j].id) {
                                    productRes[j].quantityChoose = productChoose[i].quantityChoose
                                    productRes[j].note = productChoose[i].note
                                }
                            }
                        }

                        products = productRes

                        setupRecyclerView()
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        val mAdapter = ProductRequirement2Adapter(products, true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = mAdapter

        mAdapter.onItemClick = { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, product.id)
            intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, product.id)
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
