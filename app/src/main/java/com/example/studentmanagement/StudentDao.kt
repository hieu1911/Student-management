package com.example.studentmanagement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Query("select * from students")
    suspend fun getAllStudents(): Array<Student>

    @Query("select * from students where _id = :id")
    suspend fun findStudentById(id: Int): Array<Student>

    @Insert
    suspend fun insert(student: Student): Long

    @Update
    suspend fun update(student: Student): Int

    @Delete
    suspend fun delete(student: Student): Int

    @Query("delete from students where _id = :id")
    suspend fun deleteById(id: Int): Int
}