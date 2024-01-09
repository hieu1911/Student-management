package com.example.studentmanagement

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StudentFormActivity : AppCompatActivity() {
    var myCalendar: Calendar? = Calendar.getInstance()
    lateinit var birthdayEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var idEditText: EditText
    lateinit var addressEditText: AutoCompleteTextView
    var id: Int = 0
    var name: String = ""
    var birthDay: Long = 0L
    var address: String = ""

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_form)
        val studentDao = StudentDatabase.getInstance(application).studentDao()

        val isEditing = intent.getBooleanExtra("isEditing", false)
        id = intent.getIntExtra("id", 0)
        name = intent.getStringExtra("name").toString()
        birthDay = intent.getLongExtra("birthDay", 0L)
        address = intent.getStringExtra("address").toString()

        birthdayEditText = findViewById<View>(R.id.date_of_birth) as EditText
        idEditText = findViewById(R.id.student_id_edittext)
        nameEditText = findViewById(R.id.student_name_edittext)
        addressEditText = findViewById(R.id.address_edittext)
        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar?.set(Calendar.YEAR, year)
                myCalendar?.set(Calendar.MONTH, month)
                myCalendar?.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }
        birthdayEditText.setOnClickListener(View.OnClickListener {
            myCalendar?.let { it1 ->
                DatePickerDialog(
                    this,
                    date,
                    it1.get(Calendar.YEAR),
                    myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        val textView = findViewById<AutoCompleteTextView>(R.id.address_edittext)
        val countries: Array<out String> = resources.getStringArray(R.array.countries_array)
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries).also { adapter ->
            textView.setAdapter(adapter)
        }

        val saveButton = findViewById<Button>(R.id.save_btn)
        saveButton.setOnClickListener {
            id = idEditText.text.toString().toInt()
            name = nameEditText.text.toString()
            address = addressEditText.text.toString()
            val student = Student(id, name, birthDay, address)
            lifecycleScope.launch(Dispatchers.IO) {
                if (isEditing) {
                    studentDao.update(student)
                } else {
                    studentDao.insert(student)
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val deleteButton = findViewById<Button>(R.id.delete_button)
        if (isEditing) {
            deleteButton.visibility = View.VISIBLE
        } else {
            deleteButton.visibility = View.INVISIBLE
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                studentDao.deleteById(id)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (birthDay > 0) {
            val myFormat = "MM/dd/yy"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            birthdayEditText.setText(dateFormat.format(birthDay))
        }

        if (id > 0) {
            idEditText.setText(id.toString())
        }

        nameEditText.setText(name)
        addressEditText.setText(address)
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        birthdayEditText.setText(dateFormat.format(myCalendar!!.time))
        birthDay = myCalendar!!.time.time
    }
}