package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.PriorityDAO
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context):BaseRepository() {

    private val remoteService = RetrofitClient.getService(PriorityService::class.java)
    private val taskDataBase = TaskDatabase.getDatabase(context).priorityDAO()

    fun list(listener: APIListener<List<PriorityModel>>) {
        val call = remoteService.list()
        call.enqueue(object : Callback<List<PriorityModel>>{
            override fun onResponse(call: Call<List<PriorityModel>>, response: Response<List<PriorityModel>>) {
                handleResponse(listener,response)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun list(): List<PriorityModel>{
        return taskDataBase.list()
    }


    fun saveListPriorityLocal(listPriority: List<PriorityModel>){
        taskDataBase.clear()
        taskDataBase.save(listPriority)
    }
}