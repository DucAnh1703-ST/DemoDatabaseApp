package com.example.demodatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "school.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tạo bảng students
        val createStudentTable = """
            CREATE TABLE students (
                studentID INTEGER PRIMARY KEY,
                firstName TEXT,
                lastName TEXT,
                dateOfBirth TEXT,
                city TEXT,
                phone TEXT
            );
        """
        db.execSQL(createStudentTable)

        // Tạo bảng subjects (cập nhật với subjectID)
        val createSubjectsTable = """
            CREATE TABLE subjects (
                subjectID INTEGER PRIMARY KEY AUTOINCREMENT,
                studentID INTEGER,
                name TEXT,
                score INTEGER,
                FOREIGN KEY(studentID) REFERENCES students(studentID)
            );
        """
        db.execSQL(createSubjectsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS students")
        db.execSQL("DROP TABLE IF EXISTS subjects")
        onCreate(db)
    }
}
