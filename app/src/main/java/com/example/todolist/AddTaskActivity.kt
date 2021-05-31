package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.db.DbHelper
import com.example.todolist.model.Task

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: DbHelper
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbHelper(this,findViewById(R.id.addActivity))

        val isEdit = intent.getBooleanExtra("isEdit",false)
        task = intent.getParcelableExtra<Task>("task") as Task

        if(isEdit){
            binding.edtTitle.setText(task?.title)
            binding.edtDesc.setText(task?.description)
        }

        binding.saveFab.setOnClickListener{
            when{
                binding.edtTitle.text.isNullOrEmpty() -> binding.edtTitle.error = "title required"
                binding.edtDesc.text.isNullOrEmpty() -> binding.edtDesc.error = "description required"
                else -> {
                    if(isEdit){
                        db.updateTask(task!!.id,binding.edtTitle.text.toString(),binding.edtDesc.text.toString())
                    }else{
                        db.insertTask(binding.edtTitle.text.toString(),binding.edtDesc.text.toString())
                    }
                    finish()
                }
            }

        }
    }
}