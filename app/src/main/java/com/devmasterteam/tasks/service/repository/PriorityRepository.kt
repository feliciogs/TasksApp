package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context):BaseRepository(context) {

    private val remoteService = RetrofitClient.getService(PriorityService::class.java)
    private val taskDataBase = TaskDatabase.getDatabase(context).priorityDAO()


    companion object{
        private val cache = mutableMapOf<Int,String>()
        fun getDescription(id: Int):String{
            return cache[id] ?: ""
        }
        fun setDescription(id: Int, str:String){
            cache[id] = str
        }
    }

    fun getDescription(id:Int):String{
        val cached = PriorityRepository.getDescription(id)
        return if(cached ==""){
            val description = taskDataBase.getDescription(id)
            setDescription(id,description)
            description
        }else{
            cached
        }
    }

    fun list(listener: APIListener<List<PriorityModel>>) {
        val call = remoteService.list()
        executeCall(call,listener)
    }

    fun list(): List<PriorityModel>{
        return taskDataBase.list()
    }

    fun saveListPriorityLocal(listPriority: List<PriorityModel>){
        taskDataBase.clear()
        taskDataBase.save(listPriority)
    }
}