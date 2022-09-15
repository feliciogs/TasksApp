package com.devmasterteam.tasks.service.model

import com.google.gson.annotations.SerializedName

class TaskModel {

    @SerializedName("Id")
    var id:Int = 0

    @SerializedName("PriorityId")
    var priorityID: Int = 0

    @SerializedName("Description")
    var description: String = ""

    @SerializedName("DueDate")
    var dueDate: String = ""

    @SerializedName("Complete")
    var complete:Boolean = false

    var priorityDescription: String = ""
}