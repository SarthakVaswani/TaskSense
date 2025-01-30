package com.example.taskappai.presentation

import android.app.Application
import com.example.taskappai.di.appModule
import com.example.taskappai.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaskPriorityApp : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@TaskPriorityApp)
            modules(listOf(
                appModule,
                networkModule
            ))
        }
    }

}