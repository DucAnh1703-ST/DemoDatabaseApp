package com.example.demodatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DatabaseOperations {
    @SuppressLint("Range")
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

                // Kiểm tra xem studentID đã tồn tại trong bảng students chưa
                val cursor = db.query(
                    "students",
                    arrayOf("studentID"),
                    "studentID = ?",
                    arrayOf(student.studentID.toString()),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    // Sinh viên đã tồn tại, lấy danh sách các môn học của sinh viên từ bảng subjects
                    val existingSubjectsCursor = db.query(
                        "subjects",
                        arrayOf("name"),
                        "studentID = ?",
                        arrayOf(student.studentID.toString()),
                        null,
                        null,
                        null
                    )

                    // Tạo một tập hợp chứa tên các môn học đã tồn tại của sinh viên
                    val existingSubjects = mutableSetOf<String>()
                    while (existingSubjectsCursor.moveToNext()) {
                        val subjectName = existingSubjectsCursor.getString(existingSubjectsCursor.getColumnIndex("name"))
                        existingSubjects.add(subjectName)
                    }
                    existingSubjectsCursor.close()

                    // Chuẩn bị danh sách các môn học mới để thêm vào
                    val subjectsToAdd = mutableListOf<ContentValues>()
                    for (subject in student.subjects) {
                        // Chỉ thêm môn học nếu nó chưa tồn tại
                        if (!existingSubjects.contains(subject.name)) {
                            val subjectValues = ContentValues().apply {
                                put("studentID", student.studentID)
                                put("name", subject.name)
                                put("score", subject.score)
                            }
                            subjectsToAdd.add(subjectValues)
                        }
                    }

                    // Thêm tất cả môn học chưa tồn tại vào bảng subjects trong một lần duy nhất
                    if (subjectsToAdd.isNotEmpty()) {
                        val subjectValuesArray = subjectsToAdd.toTypedArray()
                        for (values in subjectValuesArray) {
                            db.insertWithOnConflict(
                                "subjects",
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_IGNORE
                            )
                        }
                    }
                }
                cursor.close() // Đảm bảo đóng con trỏ sau khi sử dụng
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

    @SuppressLint("Range")
    fun getFirst100Students(context: Context): List<Student> {
        val dbHelper = DatabaseHelper(context.applicationContext)
        val db = dbHelper.readableDatabase

        val studentList = mutableListOf<Student>()
        val cursor = db.query(
            "students", null, null, null, null, null, "studentID ASC", "100"
        )

        while (cursor.moveToNext()) {
            val studentID = cursor.getInt(cursor.getColumnIndex("studentID"))
            val firstName = cursor.getString(cursor.getColumnIndex("firstName"))
            val lastName = cursor.getString(cursor.getColumnIndex("lastName"))
            val dateOfBirth = cursor.getString(cursor.getColumnIndex("dateOfBirth"))
            val city = cursor.getString(cursor.getColumnIndex("city"))
            val phone = cursor.getString(cursor.getColumnIndex("phone"))

            // Lấy danh sách môn học
            val subjects = mutableListOf<Subjects>()
            val subjectCursor = db.query(
                "subjects", arrayOf("subjectID", "name", "score"),
                "studentID = ?", arrayOf(studentID.toString()), null, null, null
            )

            while (subjectCursor.moveToNext()) {
                subjects.add(
                    Subjects(
                        subjectCursor.getInt(subjectCursor.getColumnIndex("subjectID")),
                        studentID,
                        subjectCursor.getString(subjectCursor.getColumnIndex("name")),
                        subjectCursor.getInt(subjectCursor.getColumnIndex("score"))
                    )
                )
            }
            subjectCursor.close()

            // Thêm sinh viên vào danh sách
            studentList.add(Student(studentID, firstName, lastName, dateOfBirth, city, phone, subjects))
        }
        cursor.close()

        return studentList
    }

}



