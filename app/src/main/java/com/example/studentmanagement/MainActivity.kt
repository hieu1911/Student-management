package com.example.studentmanagement

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Student management"
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        val studentDao = StudentDatabase.getInstance(application).studentDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val students = studentDao.getAllStudents()
            val adapter = ItemAdapter(students)
            adapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener {
                override fun onClick(position: Int) {
                    val intent = Intent(context, StudentFormActivity::class.java)
                    intent.putExtra("isEditing", true)
                    intent.putExtra("id", students[position]._id)
                    intent.putExtra("name", students[position].name)
                    intent.putExtra("birthDay", students[position].birthDay)
                    intent.putExtra("address", students[position].address)
                    startActivity(intent)
                }
            })
            val recyclerView = findViewById<RecyclerView>(R.id.student_list)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_contact -> {
                val intent = Intent(this, StudentFormActivity::class.java)
                intent.putExtra("id", 0)
                intent.putExtra("name", "")
                intent.putExtra("birthDat", 0L)
                intent.putExtra("address", "")
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}