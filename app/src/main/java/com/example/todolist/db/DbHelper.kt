package com.example.todolist.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.FontsContract
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.todolist.model.Task
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DbHelper(context: Context?,view: View): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    val context = context
    val view = view

    companion object {
        const val DATABASE_NAME = "task.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "task"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "column_title"
        const val COLUMN_DESCRIPTION = "column_description"
        const val COLUMN_TIME = "column_time"
        const val COLUMN_ISCOMPLETE = "column_iscomplete"
    }

    val query = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY," +
            "$COLUMN_TITLE TEXT," +
            "$COLUMN_DESCRIPTION TEXT," +
            "$COLUMN_TIME TEXT," +
            "$COLUMN_ISCOMPLETE TEXT" +
            ");"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insertTask(title: String,desc: String){
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE,title)
        values.put(COLUMN_DESCRIPTION,desc)
        values.put(COLUMN_ISCOMPLETE,"false")
        values.put(COLUMN_TIME,"")


        val result = db.insert(TABLE_NAME,null,values)

        if(result == -1L){
            Toast.makeText(context,"something went wrong :(",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"new task!",Toast.LENGTH_SHORT).show()
        }
    }




    fun deleteTask(id: String){
        val db = writableDatabase

        val result = db.delete(TABLE_NAME,"$COLUMN_ID LIKE ?", arrayOf(id))
        if(result == -1){
            Snackbar.make(view,"something went wrong :(",Snackbar.LENGTH_SHORT).show()
        }else{
            Snackbar.make(view,"delete success",Snackbar.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTaskComplete(id: String, isComplete: String){
        val db = writableDatabase

        val complete = if(isComplete.equals("false")) "true" else "false"
        val time = if(complete.equals("false")) "" else getCurrentDateAndTime()

        val values = ContentValues()
        values.put(COLUMN_ISCOMPLETE,complete)
        values.put(COLUMN_TIME,time)

        val result = db.update(TABLE_NAME,values,"$COLUMN_ID LIKE ?", arrayOf(id))
        if(result == -1){
            Snackbar.make(view,"something went wrong :(",Snackbar.LENGTH_SHORT).show()
        }else{
            if(complete.equals("false")){
                Snackbar.make(view,"task not complete",Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(view,"task complete in $time",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun parseCursorTask(cursor: Cursor): ArrayList<Task>{
        val listTask = ArrayList<Task>()
        with(cursor){
            while (moveToNext()){
                val id = getInt(getColumnIndex(COLUMN_ID))
                val title = getString(getColumnIndex(COLUMN_TITLE))
                val description = getString(getColumnIndex(COLUMN_DESCRIPTION))
                val time = getString(getColumnIndex(COLUMN_TIME))
                val isComplete = getString(getColumnIndex(COLUMN_ISCOMPLETE))
                listTask.add(Task(id.toString(),title,description,time,isComplete))
            }
        }

        return listTask
    }

    fun getCompleteTask(): ArrayList<Task>{

        val db = readableDatabase

        val cursor = db.query(
                TABLE_NAME,
                null,
                "${COLUMN_ISCOMPLETE} LIKE ?", arrayOf("true"),null,null,null)

        return parseCursorTask(cursor)
    }

    fun allTask(): ArrayList<Task>{
        val db = readableDatabase

        val cursor = db.query(
                TABLE_NAME,
                arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_ISCOMPLETE, COLUMN_TIME),
                null,null,null,null,null
        )
        return parseCursorTask(cursor)
    }

    fun updateTask(id: String,title: String,desc: String){
        val db = writableDatabase

        val values = ContentValues()
        values.put(COLUMN_TITLE,title)
        values.put(COLUMN_DESCRIPTION,desc)

        val result = db.update(TABLE_NAME,values,"$COLUMN_ID LIKE ?", arrayOf(id))
        if(result == -1){
            Snackbar.make(view,"something went wrong :(",Snackbar.LENGTH_SHORT).show()
        }else{
            Snackbar.make(view,"update task!",Snackbar.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateAndTime(): String{
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}