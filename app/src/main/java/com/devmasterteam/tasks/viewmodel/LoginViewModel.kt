package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser


    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email,password, object: APIListener<PersonModel>{
            override fun onSuccess(result: PersonModel) {
                savePersonPrefs(result)
                RetrofitClient.addHeader(result.token,result.personKey)

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
    fun verifyLoggedUser(){
        val tokenKey = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        RetrofitClient.addHeader(tokenKey,personKey)

        val logged = (tokenKey != "" && personKey != "" )
        _loggedUser.value = logged

        if(!logged){
            downloadPriorityList()
        }
    }

    private fun downloadPriorityList(){
        priorityRepository.list(object :APIListener<List<PriorityModel>>{
            override fun onSuccess(result: List<PriorityModel>) {
                priorityRepository.saveListPriorityLocal(result)
            }

            override fun onFailure(message: String) {

            }
        })
    }
}