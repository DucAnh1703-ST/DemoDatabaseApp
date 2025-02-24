package com.example.demodatabase

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class MyApplication : Application() {

    private var databaseService: IMyMySchoolInterface? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            databaseService = IMyMySchoolInterface.Stub.asInterface(binder)
            isBound = true
            Log.d("MyApplication", "Service connected")
//            Log.d("MyApplication", "Service connected (1) ${databaseService?.first100Students?.lastOrNull()}")

            // Gọi hàm để lấy 10 học sinh có điểm cao nhất trong môn học cụ thể
//            val subjectName = "Physics"  // Thay đổi môn học theo yêu cầu của bạn
//            val top10Students = databaseService?.getTop10StudentsBySubject(subjectName)
//
//            // Log danh sách 10 sinh viên và môn học của họ
//            top10Students?.forEach { student ->
//                val subjectsInfo = student.subjects.joinToString(", ") { "${it.name}: ${it.score}" }
//                Log.d("MyApplication", "Student: ${student.firstName} ${student.lastName}, Subjects: $subjectsInfo")
//            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            databaseService = null
            isBound = false
            Log.d("MyApplication", "Service disconnected")
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Bind to the DatabaseService when the application starts
        val serviceIntent = Intent(this, MySchoolService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        Log.d("MyApplication", "Service binding initiated")
    }

    override fun onTerminate() {
        super.onTerminate()

        // Unbind the service when the application is terminating
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
            Log.d("MyApplication", "Service unbound")
        }
    }
}