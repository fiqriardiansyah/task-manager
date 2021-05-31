package com.example.todolist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.todolist.databinding.FragmentDetailBinding
import com.example.todolist.db.DbHelper
import com.example.todolist.model.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var color: Int = R.color.blue_task
    private lateinit var addFab: FloatingActionButton
    private lateinit var db: DbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        task = arguments?.getParcelable<Task>("task") as Task
        color = arguments?.getInt("color",R.color.grey)!!

        db = DbHelper(context, activity?.findViewById(R.id.containerActivity)!!)

        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    @SuppressLint("ResourceAsColor")
    override fun onStart() {
        super.onStart()

        this.view?.setBackgroundColor(color)
        addFab = activity!!.findViewById(R.id.addFab)
        addFab?.visibility = View.INVISIBLE

        checkTaskIsComplete()
    }

    fun checkTaskIsComplete(){
        val img = this.view?.findViewById<ImageView>(R.id.imgIsComplete)
        if(task.isComplete.equals("false")){
            img?.setImageResource(R.drawable.ic_baseline_check_24)
        }else{
            img?.setImageResource(R.drawable.ic_baseline_check_complete)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitleDetail.text = task.title
        binding.tvDescDetail.text = task.description

        if(task.isComplete.equals("false")){
            binding.tvTaskComplete.visibility = View.INVISIBLE
        }else{
            binding.tvTaskComplete.visibility = View.VISIBLE
            binding.tvTaskComplete.text = resources.getString(R.string.task_complete_in,task.time)
        }

        binding.deleteFab.setOnClickListener(this)
        binding.editFab.setOnClickListener(this)
        binding.checkFab.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        addFab.visibility = View.VISIBLE
    }

    fun backToMainActivity(){
        val intent = Intent(activity?.applicationContext,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view?.id){
            binding.deleteFab.id -> {
                val mAlertDialog = AlertDialog.Builder(context)
                mAlertDialog.setTitle("DELETE")
                mAlertDialog.setMessage("delete task ${task.title} ?")
                mAlertDialog.setIcon(R.drawable.ic_baseline_warning_24)
                mAlertDialog.setPositiveButton("yes",DialogInterface.OnClickListener{ dialogInterface, i ->
                    db.deleteTask(task.id)
                    dialogInterface.dismiss()
                    activity?.supportFragmentManager?.popBackStack()
                    backToMainActivity()
                })
                mAlertDialog.setNegativeButton("no",DialogInterface.OnClickListener{ dialogInterface, i ->
                    dialogInterface.dismiss()
                })

                mAlertDialog.show()

            }
            binding.checkFab.id -> {
                db.updateTaskComplete(task.id,task.isComplete)
                activity?.supportFragmentManager?.popBackStack()
                backToMainActivity()
            }
            binding.editFab.id -> {
                val intent = Intent(activity?.applicationContext,AddTaskActivity::class.java)
                intent.putExtra("isEdit",true)
                intent.putExtra("task",task)
                startActivity(intent)
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

}