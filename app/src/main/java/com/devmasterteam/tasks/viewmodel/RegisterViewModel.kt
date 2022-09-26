package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val registerRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    fun create(name: String, email: String, password: String) {
        registerRepository.create(name,email,password, object : APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                savePersonPrefs(result)

                RetrofitClient.addHeader(result.token,result.personKey)

                _user.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _user.value = ValidationModel(message)
            }

        })
    }

    fun savePersonPrefs(personModel:PersonModel){
        securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY,personModel.token)
        securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,personModel.personKey)
        securityPreferences.store(TaskConstants.SHARED.PERSON_NAME,personModel.name)
    }

}