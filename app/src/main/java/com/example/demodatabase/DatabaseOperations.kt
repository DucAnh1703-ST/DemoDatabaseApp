package com.example.demodatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.demodatabase.model.Student

// Hàm lưu sinh viên và môn học vào cơ sở dữ liệu
class DatabaseOperations {
    fun saveStudentToDatabase(students: List<Student>, context: Context) {
        val dbHelper = DatabaseHelper(context.applicationContext)
        val db = dbHelper.writableDatabase

        // Bắt đầu giao dịch để cải thiện hiệu suất
        db.beginTransaction()

        try {
            // Lưu từng sinh viên vào bảng students
            for (student in students) {
                // Thêm sinh viên vào bảng students
                val studentValues = ContentValues().apply {
                    put("studentID", student.studentID)
                    put("firstName", student.firstName)
                    put("lastName", student.lastName)
                    put("dateOfBirth", student.dateOfBirth)
                    put("city", student.city)
                    put("phone", student.phone)
                }

                // Chèn sinh viên vào bảng students, sử dụng OnConflict để tránh lỗi trùng lặp
                val studentId = db.insertWithOnConflict(
                    "students",
                    null,
                    studentValues,
                    SQLiteDatabase.CONFLICT_IGNORE
                )

                // Lưu các môn học của sinh viên vào bảng subjects
                for (subject in student.subjects) {
                    val subjectValues = ContentValues().apply {
                        put("studentID", student.studentID)
                        put("name", subject.name)
                        put("score", subject.score)
                    }

                    // Thêm môn học vào bảng subjects
                    db.insertWithOnConflict(
                        "subjects",
                        null,
                        subjectValues,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }

            // Cam kết giao dịch (commit)
            db.setTransactionSuccessful()

            // Log thành công nếu tất cả dữ liệu đã được lưu
            Log.d("Database", "Lưu tất cả sinh viên và môn học thành công")
        } catch (e: Exception) {
            // Log lỗi nếu có ngoại lệ xảy ra
            Log.e("Database", "Lỗi khi lưu sinh viên và môn học vào cơ sở dữ liệu: ${e.message}")
        } finally {
            // Kết thúc giao dịch và đóng kết nối database
            db.endTransaction()
        }
    }
}

