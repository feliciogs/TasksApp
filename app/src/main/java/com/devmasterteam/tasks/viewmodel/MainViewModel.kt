package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val securityRepository = SecurityPreferences(application.applicationContext)

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    fun logout(){
        securityRepository.remove(TaskConstants.SHARED.TOKEN_KEY)
        securityRepository.remove(TaskConstants.SHARED.PERSON_KEY)
        securityRepository.remove(TaskConstants.SHARED.PERSON_NAME)
    }

    fun loadNameUser(){
        _name.value = securityRepository.get(TaskConstants.SHARED.PERSON_NAME)
    }
}