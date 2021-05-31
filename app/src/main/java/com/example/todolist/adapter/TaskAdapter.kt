package com.example.todolist.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.model.Task
import kotlin.random.Random

class TaskAdapter(context: Context): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    val context = context

    var listTask = ArrayList<Task>()

    private lateinit var onClickItemListener: OnClickItemListener

    interface OnClickItemListener {
        fun onClick(task: Task,color: Int)
    }

    fun onClickItemCallback(onClickItemListener: OnClickItemListener){
        this.onClickItemListener = onClickItemListener
    }

    fun setData(list: ArrayList<Task>){
        this.listTask = list
    }

    inner class ViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listTask[position]){
                binding.tvTitle.text = title
                binding.tvTime.text = time
                binding.tvDesc.text = description

                if(isComplete.equals("true")){
                    binding.imgCheck.visibility = View.VISIBLE
                }else{
                    binding.imgCheck.visibility = View.INVISIBLE
                }
            }

            val background = holder.itemView.background
            val shapeDrawable = background as GradientDrawable
            val color = randomColor()
            shapeDrawable.setColor(color)

            holder.itemView.setBackgroundDrawable(background)

            holder.itemView.setOnClickListener{
                onClickItemListener.onClick(listTask[position],color)
            }

        }
    }

    fun randomColor(): Int{
        val colors = context.resources.getIntArray(R.array.task_colors)
        var randomColor = colors[Random.nextInt(colors.size)]
        return randomColor
    }

    override fun getItemCount(): Int = listTask.size
}