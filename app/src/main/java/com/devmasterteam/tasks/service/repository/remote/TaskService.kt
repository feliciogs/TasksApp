package com.devmasterteam.tasks.service.repository.remote


import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    // http://devmasterteam.com/CursoAndroidAPI/Task

    @GET("Task")
    fun list(): Call<List<TaskModel>>

    @GET("Next7Days")
    fun listNext(): Call<List<TaskModel>>

    @GET("Overdue")
    fun listOverdue(): Call<List<TaskModel>>

    @GET("Task/{id}")
    fun load(@Path(value = "id", encoded = true) id:Int): Call<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    fun create(
        @Field("PriorityId") priorityID:Int,
        @Field("Description") description:String,
        @Field("DueDate") dueDate:String,
        @Field("Complete") complete:Boolean
    ): Call<Boolean>

    @PUT("Task")
    @FormUrlEncoded
    fun update(
        @Field("Id") id:Int,
        @Field("PriorityId") priorityID:Int,
        @Field("Description") description:String,
        @Field("DueDate") dueDate:String,
        @Field("Complete") complete:Boolean
    ): Call<Boolean>

    @PUT("Task/Complete")
    @FormUrlEncoded
    fun complete(
        @Field("Id") id:Int
    ): Call<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    fun undo(
        @Field("Id") id:Int
    ): Call<Boolean>

    @DELETE("Task")
    @FormUrlEncoded
    fun delete(
        @Field("Id") id:Int
    ): Call<Boolean>
}