package com.example.taskreminder

import android.app.Activity
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskreminder.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity(), OnDateSetListener, OnTimeSetListener {
    private lateinit var binding: ActivityAddTaskBinding
    private var title = ""
    private var repeat = ""
    private var date = ""
    private var time = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val repeatArray = resources.getStringArray(R.array.repeats)
        with(binding){
            val repeatAdapter = ArrayAdapter(
                this@AddTaskActivity,
                android.R.layout.simple_spinner_item,
                repeatArray
            )
            repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            inputRepeat.adapter = repeatAdapter

            inputRepeat.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    repeat = repeatArray.get(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            selectDate.setOnClickListener {
                com.example.taskreminder.dialog.DatePicker().show(supportFragmentManager, "datePicker")
            }
            selectTime.setOnClickListener {
                com.example.taskreminder.dialog.TimePicker().show(supportFragmentManager, "timePicker")
            }
            addTaskButton.setOnClickListener {
                title = inputTitle.text.toString()
                var valid = true
                if(title.length == 0){
                    Toast.makeText(this@AddTaskActivity, "Anda harus mengisi judul task!", Toast.LENGTH_SHORT).show()
                    valid = false
                }
                if(repeat.length == 0){
                    Toast.makeText(this@AddTaskActivity, "Anda harus mengisi perulangan task!", Toast.LENGTH_SHORT).show()
                    valid = false
                }
                if(date.length == 0){
                    Toast.makeText(this@AddTaskActivity, "Anda harus mengisi tanggal task!", Toast.LENGTH_SHORT).show()
                    valid = false
                }
                if(time.length == 0){
                    Toast.makeText(this@AddTaskActivity, "Anda harus mengisi waktu task!", Toast.LENGTH_SHORT).show()
                    valid = false
                }
                if(valid){
                    val builder = AlertDialog.Builder(this@AddTaskActivity)
                    builder.setTitle("SimpliRemind")
                    builder.setMessage("Do yo want to add this as new task?")
                    builder.setPositiveButton("Yes"){ _, _ ->
                        val taskData = arrayOf(title, repeat, date, time)
                        val intent = Intent()
                        intent.putExtra("EXTRA_TASK", taskData)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    builder.setNegativeButton("No"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        binding.dateValue.setText("$p3/${p2+1}/$p1")
        date = "$p3/${p2+1}/$p1"
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val hour = p1.toString()
        val minute = p2.toString()
        binding.hoursValue.setText(hour)
        binding.minutesValue.setText(minute)
        time = "$hour:$minute"
    }
}