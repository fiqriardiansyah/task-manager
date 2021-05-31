package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.DbHelper
import com.example.todolist.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbHelper(this,findViewById(R.id.containerActivity))

        adapter = TaskAdapter(this)
        binding.rvTask.layoutManager = LinearLayoutManager(this)
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = adapter

        binding.addFab.setOnClickListener{
            val intent = Intent(this@MainActivity,AddTaskActivity::class.java)
            intent.putExtra("isEdit",false)
            intent.putExtra("task",Task("","","","",""))
            startActivity(intent)
        }

        adapter.onClickItemCallback(object: TaskAdapter.OnClickItemListener{
            override fun onClick(task: Task,color: Int) {
                val mDetailFragment = DetailFragment()
                val mFragmentManager = supportFragmentManager
                val fragment = mFragmentManager.findFragmentByTag(DetailFragment::class.java.simpleName)

                val bundle = Bundle()
                bundle.putParcelable("task",task)
                bundle.putInt("color",color)
                mDetailFragment.arguments = bundle

                if(fragment !is DetailFragment){
                    mFragmentManager.beginTransaction().apply {
                        add(R.id.containerActivity,mDetailFragment,DetailFragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val listTask = db.allTask()
        val completeTask = db.getCompleteTask().size

        adapter.setData(listTask)
        adapter.notifyDataSetChanged()

//      update information task
        binding.tvTotalTask.text = resources.getString(R.string.total_task,listTask.size.toString())

        binding.circularProgressBar.apply {
            progressMax = listTask.size.toFloat()
            setProgressWithAnimation(completeTask.toFloat(), 1000) // =1s
        }

        binding.tvTaskDone.text = "$completeTask/${listTask.size}"
    }
}