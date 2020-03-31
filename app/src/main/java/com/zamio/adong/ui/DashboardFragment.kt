package com.zamio.adong.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.elcom.com.quizupapp.utils.PreferUtils
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import com.zamio.adong.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_dashboard.setOnClickListener {
            val preferUtils = PreferUtils()
            preferUtils.setToken(context!!,"")
            ConstantsApp.BASE64_AUTH_TOKEN = ""
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
    }




}
