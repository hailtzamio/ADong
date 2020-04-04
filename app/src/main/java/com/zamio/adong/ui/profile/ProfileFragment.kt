package com.zamio.adong.ui.profile


import RestClient
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elcom.com.quizupapp.ui.fragment.BaseFragment
import com.elcom.com.quizupapp.ui.network.RestData
import com.elcom.com.quizupapp.utils.PreferUtils
import com.squareup.picasso.Picasso
import com.zamio.adong.R
import com.zamio.adong.model.Profile
import com.zamio.adong.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : BaseFragment() {

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

            //            OneSignal.clearOneSignalNotifications()

            PreferUtils().setToken(context!!, "")
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity!!.finish()
        }

        lnUpdateProfile.setOnClickListener {
            //            startActivityForResult(Intent(context, UpdateProfileActivity::class.java),1000)
        }

        lnChangePassword.setOnClickListener {
            startActivityForResult(Intent(context, ChangePasswordActivity::class.java), 1000)
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

                    val profile = response.body()!!.data

                    if (profile != null) {
                        tvName.text = profile.fullName
                        tvEmail.text = profile.email
                        tvPhone.text = profile.phone


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
                        Picasso.get().load(profile.avatarUrl).into(imvAva)
                    }

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getProfile()
    }
}



