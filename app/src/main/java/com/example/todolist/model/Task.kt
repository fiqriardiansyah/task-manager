package com.example.todolist.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task (
    var id: String,
    var title: String,
    var description: String,
    var time: String,
    var isComplete: String
): Parcelable