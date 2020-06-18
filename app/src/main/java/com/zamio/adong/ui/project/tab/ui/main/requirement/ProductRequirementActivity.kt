package com.zamio.adong.ui.project.tab.ui.main.requirement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elcom.com.quizupapp.ui.activity.BaseActivity
import com.zamio.adong.R
import com.zamio.adong.network.ConstantsApp
import kotlinx.android.synthetic.main.item_header_layout.*

class ProductRequirementActivity : BaseActivity() {

    var id = 0
    val textFragment = ProductRequirementFragment()
    override fun getLayout(): Int {
       return R.layout.activity_product_requirement
    }

    override fun initView() {
        tvTitle.text = "Yêu Cầu Vật Tư"
        rightButton.setOnClickListener {
            val intent = Intent(this, CreateProductRequirementActivity::class.java)
            intent.putExtra(ConstantsApp.KEY_VALUES_ID, id)
            startActivityForResult(intent, 1000)
        }
    }

    override fun initData() {
        if (intent.hasExtra(ConstantsApp.KEY_VALUES_ID)) {
            id = intent.getIntExtra(ConstantsApp.KEY_VALUES_ID, 1)
            textFragment.setProjectId(id)
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragment_container,textFragment)
            transaction.commit()
        }
    }

    override fun resumeData() {

    }

    fun getProjectId(): Int {
        return id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 101){
            textFragment.getData(0)
        }
    }
}
