package com.quiz.videoconsolas.application

import android.app.Application

class AdivinaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}