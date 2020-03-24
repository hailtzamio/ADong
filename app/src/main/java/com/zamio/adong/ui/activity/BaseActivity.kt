package com.elcom.com.quizupapp.ui.activity

import android.content.Intent
import android.os.Bundle

import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zamio.adong.R
import com.zamio.adong.utils.ProgressDialogUtils


abstract class BaseActivity : FragmentActivity() {
    protected var TAG = ""
    protected val TAG_ACTIVITY = 999

    protected abstract fun getLayout(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun resumeData()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayout())
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        resumeData()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startActivityAndClearAllTask(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun startActivityAndClearAllTask(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivityAndClearAllTask(intent)
    }

    fun startActivityAndClearAllTask(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(this, cls)
        intent.putExtras(bundle)
        startActivityAndClearAllTask(intent)
    }

    fun startActivityDefault(intent: Intent) {
        super.startActivity(intent)
    }

    fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun startActivity(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(this, cls)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        val intent = Intent(this, cls)
        startActivityForResult(intent, requestCode)
    }

    fun startActivityForResult(cls: Class<*>, requestCode: Int, bundle: Bundle) {
        val intent = Intent(this, cls)
        intent.putExtras(bundle)
        startActivityForResult(intent, requestCode)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun openFragment(resId: Int, fragmentClazz: Class<out Fragment>, addBackStack: Boolean) {
        openFragment(resId, fragmentClazz, null, addBackStack)
    }

    fun openFragmentBundle(resId: Int, fragmentClazz: Class<out Fragment>, bundle: Bundle, addBackStack: Boolean) {
        openFragment(resId, fragmentClazz, bundle, addBackStack)
    }

    fun openFragment(resId: Int, fragmentClazz: Class<out Fragment>, args: Bundle?, addBackStack: Boolean) {
        val manager = supportFragmentManager
        val tag = fragmentClazz.name
        try {
            val fragment: Fragment
            try {
                fragment = fragmentClazz.newInstance()
                if (args != null) {
                    fragment.arguments = args
                }
                val transaction = manager.beginTransaction()
                //transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.replace(resId, fragment, tag)
                if (addBackStack) {
                    transaction.addToBackStack(tag)
                }
                transaction.commitAllowingStateLoss()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun startActivityUp(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_up, R.anim.no_change)
    }

    fun finishUp() {
        super.finish()
        overridePendingTransition(R.anim.no_change, R.anim.slide_bottom)
    }

    fun showProgessDialog() {
        ProgressDialogUtils.showProgressDialog(this, 0, 0)
    }

    fun dismisProgressDialog() {
        ProgressDialogUtils.dismissProgressDialog()
    }

}

