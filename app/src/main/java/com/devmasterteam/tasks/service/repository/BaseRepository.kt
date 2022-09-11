package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener

import com.google.gson.Gson
import retrofit2.Response

open class BaseRepository {

    private fun failResponse(str:String):String{
        return Gson().fromJson(str,String::class.java)
    }

    fun <T> handleResponse(listener: APIListener<T> , response: Response<T>){
        if(response.code() == TaskConstants.HTTP.SUCCESS){
            response.body()?.let { listener.onSuccess(it) }
        }else{
            listener.onFailure(failResponse(response.errorBody()!!.string()))
        }
    }
}