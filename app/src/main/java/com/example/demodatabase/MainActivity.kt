package com.example.demodatabase

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Đọc dữ liệu từ file JSON và lưu vào cơ sở dữ liệu
        val jsonFilePath = "fresher.json" // Đường dẫn tới file JSON
        processAndSaveData(jsonFilePath, this)

        // Hiển thị thông tin sinh viên và môn học từ cơ sở dữ liệu
        displayStudentsAndSubjects(this)
    }

    fun processAndSaveData(jsonFilePath: String, context: Context) {
        val startTime = System.currentTimeMillis() // Bắt đầu thời gian
        // Tạo một đối tượng JsonParser và gọi phương thức parseJsonToStudents

        val jsonParser = JsonParser()
        val students = jsonParser.parseJsonFromAssets(applicationContext, "fresher.json")

        // Lưu danh sách sinh viên vào cơ sở dữ liệu
        val dataBaseOptionDemo = DatabaseOperations()
        dataBaseOptionDemo.saveStudentToDatabase(students, applicationContext)

        val endTime = System.currentTimeMillis() // Thời gian kết thúc
        Log.d("ImportJson", "Tổng thời gian xử lý: ${endTime - startTime} ms")
    }

    fun displayStudentsAndSubjects(context: Context) {
        // Lấy danh sách sinh viên và môn học
        val databaseOperations = DatabaseOperations()
        val students = databaseOperations.getFirst100Students(context)

        // Duyệt qua từng sinh viên và hiển thị log
        for (student in students) {
            Log.d("StudentInfo", "Sinh viên: ${student.firstName} ${student.lastName}, Mã sinh viên: ${student.studentID}")

            // Duyệt qua danh sách môn học của sinh viên và hiển thị log
            for (subject in student.subjects) {
                Log.d("SubjectInfo", "Môn học: ${subject.name}, Điểm: ${subject.score}, Mã môn học: ${subject.subjectID}, Mã sinh viên: ${subject.studentID}")
            }
        }
    }
}
