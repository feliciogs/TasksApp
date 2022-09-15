package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority: List<PriorityModel> = listOf()
    private var taskID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()
        loadDataFromActivity()
        observe()
        // Layout
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.button_date){
            handleDate()
        }else if(v.id == R.id.button_save){
            handleSave()
        }
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)

        binding.buttonDate.text = dateFormat.format(calendar.time)
    }

    private fun observe(){
        viewModel.priorityList.observe(this){
            listPriority = it
            val list = mutableListOf<String>()
            for (p in it){
                list.add(p.description)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.saveTask.observe(this){
            if(it.getStatus()){
                if(taskID == 0){
                    toast("Tarefa criada com sucesso")
                }else{
                    toast("Tarefa Atualizada com sucesso")
                }
                finish()
            }else{
                toast(it.getMessage())
            }
        }

        viewModel.task.observe(this){
            setTask(it)
        }

        viewModel.taskLoad.observe(this){
            if(!it.getStatus()){
                toast(it.getMessage())
                finish()
            }
        }
    }

    private fun toast(str:String){
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
    }

    private fun setTask(taskModel: TaskModel) {
        binding.editDescription.setText(taskModel.description)
        binding.checkComplete.isChecked = taskModel.complete
        val date = SimpleDateFormat("yyyy-MM-dd").parse(taskModel.dueDate)
        binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        binding.spinnerPriority.setSelection(getIndex(taskModel.priorityID))
    }

    private fun getIndex(priorityID:Int):Int{
        var index = 0
        for(l in listPriority){
            if(l.id== priorityID){
                break
            }
            index++
        }
        return index
    }

    private fun handleSave(){
        val task = TaskModel().apply {
            this.id = taskID
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityID = listPriority[index].id
        }
        viewModel.saveTask(task)
    }


    private fun handleDate(){
        val calendar = Calendar.getInstance()
        calendar.get(Calendar.YEAR)
        DatePickerDialog(this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if(bundle != null){
            taskID = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskID)
        }
    }
}