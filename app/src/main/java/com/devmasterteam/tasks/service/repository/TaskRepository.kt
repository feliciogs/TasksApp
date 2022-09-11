package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context): BaseRepository() {

    private val remoteService = RetrofitClient.getService(TaskService::class.java)

    fun create(taskModel: TaskModel, listener:APIListener<Boolean>){
        val call = remoteService.create(taskModel.id,taskModel.description,taskModel.dueDate,taskModel.complete)
        call.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(listener,response)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }
}