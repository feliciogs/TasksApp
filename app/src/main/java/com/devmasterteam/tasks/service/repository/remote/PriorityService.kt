package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET


interface PriorityService {
    //  http://devmasterteam.com/CursoAndroidAPI/Priority

    @GET("Priority")
    fun list(): Call<List<PriorityModel>>
}