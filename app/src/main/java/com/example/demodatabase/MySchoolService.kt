package com.example.demodatabase

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MySchoolService : Service() {
    private lateinit var dataBaseOptionDemo : DatabaseOperations

    override fun onCreate() {
        super.onCreate()
        // Đọc dữ liệu từ file JSON và lưu vào cơ sở dữ liệu
        val jsonFilePath = "fresher.json" // Đường dẫn tới file JSON
        dataBaseOptionDemo = DatabaseOperations()
        processAndSaveData(jsonFilePath, this)
    }

    private val binder = object : IMyMySchoolInterface.Stub() {
//        override fun getAllStudents(): List<Student> {
//            return databaseHelper.getAllStudents()
//        }
//
//        override fun getStudentsByName(name: String?): List<Student> {
//            return databaseHelper.getStudentsByName(name!!)
//        }
//
//        override fun getTop10StudentByObject(nameSubject: String?): List<Student> {
//            return databaseHelper.getTop10StudentsBySubject(nameSubject!!)
//        }
//
//        override fun getTop10StudentSumAByCity(nameCity: String?): List<Student> {
//            return databaseHelper.getTop10SumAByCity(nameCity!!)
//        }
//
//        override fun getTop10StudentSumBByCity(nameCity: String?): List<Student> {
//            return databaseHelper.getTop10SumBByCity(nameCity!!)
//        }
//
        override fun getFirst100Students(): List<Student> {
            return dataBaseOptionDemo.getFirst100Students(this@MySchoolService)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
//        return null
    }

    fun processAndSaveData(jsonFilePath: String, context: Context) {
        val startTime = System.currentTimeMillis() // Bắt đầu thời gian
        // Tạo một đối tượng JsonParser và gọi phương thức parseJsonToStudents

        val jsonParser = JsonParser()
        val students = jsonParser.parseJsonFromAssets(applicationContext, "fresher.json")

        // Lưu danh sách sinh viên vào cơ sở dữ liệu
        dataBaseOptionDemo.saveStudentToDatabase(students, applicationContext)

        val endTime = System.currentTimeMillis() // Thời gian kết thúc
        Log.d("ImportJson", "Tổng thời gian xử lý: ${endTime - startTime} ms")
    }
}