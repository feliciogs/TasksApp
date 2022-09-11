package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()

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

    }

    private fun handleSave(){
        val task = TaskModel().apply {
            this.id = 0
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


}