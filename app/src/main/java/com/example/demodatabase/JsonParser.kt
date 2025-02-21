package com.example.demodatabase

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.InputStreamReader

// Cập nhật hàm để trả về một danh sách các Student
class JsonParser {
    // Sử dụng assets để đọc file JSON
    fun parseJsonFromAssets(context: Context, fileName: String): List<Student> {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName) // Đọc file từ assets

        val reader = InputStreamReader(inputStream)
        val gson = Gson()

        // Parse JSON thành danh sách sinh viên
        val students = gson.fromJson(reader, Array<Student>::class.java).toList()

        // In số lượng sinh viên ra log
        Log.d("JsonParser", "Số lượng sinh viên: ${students.size}")

        return students
    }
}