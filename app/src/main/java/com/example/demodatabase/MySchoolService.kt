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

        // Xử lý thêm các trường hợp không tim được Student, người dùng nhập vào chuỗi rỗng





        override fun getStudentByPriority(firstName: String?, nameCity: String?): Student? {
            return dataBaseOptionDemo.getStudentByPriority(firstName!!, nameCity!!, this@MySchoolService)
        }

        override fun getTop10StudentsBySumA(nameCity: String?): List<Student> {
            return dataBaseOptionDemo.getTop10StudentsBySumA(nameCity ?: "",this@MySchoolService)
        }

        override fun getTop10StudentsBySumB(nameCity: String?): List<Student> {
            return dataBaseOptionDemo.getTop10StudentsBySumB(nameCity ?: "",this@MySchoolService)
        }

        override fun getTop10StudentsBySubject(nameSubject: String?): List<Student> {
            return dataBaseOptionDemo.getTop10StudentsBySubject(nameSubject!!,this@MySchoolService)
        }

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