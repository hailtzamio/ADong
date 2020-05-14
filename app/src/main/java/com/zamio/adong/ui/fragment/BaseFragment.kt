package com.elcom.com.quizupapp.ui.fragment

import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zamio.adong.R
import com.zamio.adong.utils.ProgressDialogUtils


/**
 * Created by Hailpt on 4/23/2018.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    fun openFragment(newFragment:Fragment) {
//        val transaction = fragmentManager!!.beginTransaction()
////        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
//        transaction.replace(R.id.frame_layout, newFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        val backStateName = fragment.javaClass.name
//
//        val manager = activity!!.getSupportFragmentManager()
//        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
//
//        if (!fragmentPopped) { //fragment not in back stack, create it.
//            val ft = manager.beginTransaction()
//            ft.replace(R.id.frame_layout, fragment)
//            ft.addToBackStack(backStateName)
//            ft.commit()
//        }
//    }


    fun showProgessDialog() {
        ProgressDialogUtils.showProgressDialog(context, 0, 0)
    }

    fun dismisProgressDialog() {
        ProgressDialogUtils.dismissProgressDialog()
    }

    fun showToast(content:String){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

}