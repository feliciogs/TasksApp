package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(context: Context): BaseRepository(context) {

    private val remoteService = RetrofitClient.getService(TaskService::class.java)

    fun list(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.list()
        executeCall(call,listener)
    }

    fun listNext(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.listNext()
        executeCall(call,listener)
    }

    fun listOverdue(listener: APIListener<List<TaskModel>>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.listOverdue()
        executeCall(call,listener)
    }

    fun delete(id:Int,listener:APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.delete(id)
        executeCall(call,listener)
    }

    fun create(taskModel: TaskModel, listener:APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.create(taskModel.priorityID,taskModel.description,taskModel.dueDate,taskModel.complete)
        executeCall(call,listener)
    }
    fun update(taskModel: TaskModel, listener:APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.update(taskModel.id,taskModel.priorityID,taskModel.description,taskModel.dueDate,taskModel.complete)
        executeCall(call,listener)
    }

    fun complete(id:Int,listener:APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.complete(id)
        executeCall(call,listener)
    }

    fun undo(id:Int,listener:APIListener<Boolean>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = remoteService.undo(id)
        executeCall(call,listener)
    }

    fun load(id:Int,listener:APIListener<TaskModel>){
        if(!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        
        val call = remoteService.load(id)
        executeCall(call,listener)
    }
}