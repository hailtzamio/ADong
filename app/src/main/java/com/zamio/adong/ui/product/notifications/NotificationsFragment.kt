package com.zamio.adong.ui.product.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elcom.com.quizupapp.utils.PreferUtils
import com.zamio.adong.MainActivity
import com.zamio.adong.R
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnChange.setOnClickListener {

            val preferUtils = PreferUtils()
            val userId = preferUtils.getUserId(context!!)

            if (userId.equals("product")){
                preferUtils.setUserId(context!!, "drive")
            } else {
                preferUtils.setUserId(context!!, "product")
            }

            triggerRebirth(context!!)

//            val mStartActivity = Intent(context, MainActivity::class.java)
//            val mPendingIntentId = 123456
//            val mPendingIntent = PendingIntent.getActivity(
//                context,
//                mPendingIntentId,
//                mStartActivity,
//                PendingIntent.FLAG_CANCEL_CURRENT
//            )
//            val mgr = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            mgr[AlarmManager.RTC, System.currentTimeMillis()] = mPendingIntent
//            System.exit(0)
        }
    }

    private fun triggerRebirth(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.putExtra("KEY_RESTART_INTENT", nextIntent)
        context.startActivity(intent)
//        if (context is Activity) {
//            context.finish()
//        }
//        Runtime.getRuntime().exit(0)
    }
}
