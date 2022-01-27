package com.quiz.videoconsolas.ui.select

import android.os.Bundle
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.base.BaseActivity

class SelectActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerSelect, SelectFragment.newInstance())
                .commitNow()
        }
    }
}