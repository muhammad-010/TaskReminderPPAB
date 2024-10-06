package com.example.taskreminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskreminder.databinding.ActivityMainBinding
import com.example.taskreminder.databinding.TaskItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var taskDataList = ArrayList<Array<String>>()
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            var data = result.data
            val taskData = data?.getStringArrayExtra("EXTRA_TASK")
            if(taskData != null){
                if(binding.imgIlustrasi.visibility == View.VISIBLE){
                    binding.imgIlustrasi.visibility = View.GONE
                }
                taskDataList.add(taskData)
                addTaskItem(taskData.get(0), taskData.get(1), taskData.get(2), taskData.get(3))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding){
            addTaskButton.setOnClickListener {
                val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
                launcher.launch(intent)
            }

            if(taskDataList.size > 0){

            }
        }
    }

    fun addTaskItem(title: String, repeat: String, date: String, time: String) {
        val taskBinding = TaskItemBinding.inflate(layoutInflater)

        taskBinding.taskTitle.text = title
        taskBinding.taskRepeat.text = repeat
        taskBinding.taskDate.text = date
        taskBinding.taskTime.text = time

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 32
        }

        taskBinding.root.layoutParams = layoutParams

        binding.taskContainer.addView(taskBinding.root)
    }
}