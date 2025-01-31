package com.example.taskappai.di

import androidx.room.Room
import com.example.taskappai.data.local.TaskDB
import com.example.taskappai.data.remote.api.DeepSeekRepository
import com.example.taskappai.data.remote.api.DeepSeekService
import com.example.taskappai.domain.repository.TaskRepository
import com.example.taskappai.data.repository.TaskRepositoryImpl
import com.example.taskappai.presentation.screen.TaskViewModel
import com.example.taskappai.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            TaskDB::class.java,
            "tasks.db"
        ).build()
    }

    single { get<TaskDB>().taskDao }

    single<TaskRepository> {
        TaskRepositoryImpl(
            taskDao = get(),
            deepSeekRepository = get()
        )
    }

    viewModel {
        TaskViewModel(get())
    }
}
val networkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(DeepSeekService::class.java)
    }

    single { DeepSeekRepository(get()) }
}