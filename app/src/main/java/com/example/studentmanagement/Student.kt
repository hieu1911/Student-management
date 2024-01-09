package com.example.studentmanagement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "students")
data class Student (
    @PrimaryKey
    val _id: Int = 0,
    val name: String,
    @ColumnInfo(name = "date_of_birth")
    val birthDay: Long,
    val address: String
)