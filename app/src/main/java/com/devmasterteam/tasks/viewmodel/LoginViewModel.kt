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

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email,password, object: APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                savePersonPrefs(result)
                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                 _login.value = ValidationModel(message)
            }
        })
    }

    fun savePersonPrefs(personModel:PersonModel){
        securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY,personModel.token)
        securityPreferences.store(TaskConstants.SHARED.PERSON_KEY,personModel.personKey)
        securityPreferences.store(TaskConstants.SHARED.PERSON_NAME,personModel.name)
    }



    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
    }

}