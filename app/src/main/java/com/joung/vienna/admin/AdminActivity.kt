package com.joung.vienna.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joung.vienna.R

class AdminActivity : AppCompatActivity(), AdminContract.View {

    private lateinit var mPresenter: AdminPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_setting)

        mPresenter = AdminPresenter(this@AdminActivity)
    }

}