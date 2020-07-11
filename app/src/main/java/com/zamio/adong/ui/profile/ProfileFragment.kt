package com.zamio.adong.ui.profile


import RestClient
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.utils.PreferUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zamio.adong.R
import com.zamio.adong.model.Profile
import com.zamio.adong.ui.activity.LoginActivity
import com.zamio.adong.utils.ProgressDialogUtils
import kotlinx.android.synthetic.main.activity_change_information.*
import kotlinx.android.synthetic.main.activity_create_worker.*
import kotlinx.android.synthetic.main.fragment_profile.*
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
 *
 */
class ProfileFragment : BaseFragment() {

    var avatarExtId = ""
    var profile:Profile? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()

        btnLogout.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {

                        DialogInterface.BUTTON_POSITIVE -> {
                            PreferUtils().setToken(context!!, "")
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            activity!!.finish()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Đăng xuất").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show()
        }

        lnUpdateProfile.setOnClickListener {
            startActivityForResult(Intent(context, UpdateProfileActivity::class.java),1000)
        }

        lnChangePassword.setOnClickListener {
            startActivityForResult(Intent(context, ChangePasswordActivity::class.java), 1000)
        }

        imvAva.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity!!)
        }


    }

    private fun getProfile() {

        showProgessDialog()
        RestClient().getInstance().getRestService().getProfile().enqueue(object :
            Callback<RestData<Profile>> {
            override fun onFailure(call: Call<RestData<Profile>>?, t: Throwable?) {
                dismisProgressDialog()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RestData<Profile>>?,
                response: Response<RestData<Profile>>?
            ) {
                dismisProgressDialog()
                if (response?.body() != null && response.body()!!.data != null) {

                    profile = response.body()!!.data

                    if (profile != null) {
                        tvName.text = profile!!.fullName ?: "---"
                        tvEmail.text = profile!!.email ?: "---"
                        tvPhone.text = profile!!.phone ?: "---"



//                        if(profile.province != null){
//                            var address = ""
//                            if(profile.ward != null){
//                                address = profile.ward!!.name!!
//                            }
//
//                            if(profile.district != null){
//                                address += " - " + profile.district!!.name!!
//                            }
//
//                            if(address != ""){
//                                address += " - " + profile.province!!.name!!
//                            } else {
//                                address = profile.province!!.name!!
//                            }
//
//                            tvAddress.text = address
//                        }

//                        Picasso.get()
//                            .load(profile.avatarUrl.toString())
//                            .resize(50, 50)
//                            .placeholder(R.drawable.ava)
//                            .centerCrop()
//                            .into(imvAva)
                        if(profile!!.avatarUrl != null && profile!!.avatarUrl != "") {
//                            Picasso.get().load(profile.avatarUrl).into(imvAva)

//                            Picasso.get()
//                                .load(profile.avatarUrl)
//                                .error(R.drawable.ava)
//                                .networkPolicy(NetworkPolicy.OFFLINE)
//                                .into(imvAva);
                            Picasso.get().load(profile!!.avatarUrl).error(R.drawable.ava).into(imvAva)
                        }

                    }

                }
            }
        })
    }

    fun updateProfile(){

        val json = JsonObject()
        json.addProperty("fullName",profile!!.fullName)
        json.addProperty("email",profile!!.email)
        json.addProperty("phone",profile!!.phone)
        json.addProperty("avatarExtId",profile!!.avatarExtId)

        ProgressDialogUtils.showProgressDialog(context, 0, 0)
        RestClient().getInstance().getRestService().updateMyProfile(json).enqueue(object :
            Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                ProgressDialogUtils.dismissProgressDialog()
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                ProgressDialogUtils.dismissProgressDialog()
                if(response?.body() != null) {
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show()
                    activity!!.onBackPressed()
                } else {
                    val obj = JSONObject(response!!.errorBody().string())
                    showToast(obj["message"].toString())
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
        RestClient().getRestService().updateProfile(body).enqueue(object :
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
//                    showToast("Tạo vật tư thành công " + response.body().data)
                    val idOb = response.body().data!!.asJsonObject
                    profile!!.avatarExtId = idOb["id"].asString
                    updateProfile()
                } else {
                    if (response!!.errorBody() != null) {
                        val obj = JSONObject(response.errorBody().string())
                        showToast(obj["message"].toString())
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                val file = File(resultUri.path!!)
                uploadImage(file)
                cropImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}



