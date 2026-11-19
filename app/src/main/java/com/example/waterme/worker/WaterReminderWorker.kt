
package com.example.waterme.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WaterReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        // Lấy tên cây cần tưới
        val plantName = inputData.getString(nameKey)
            ?: "cây trồng"

        // Nội dung thông báo bằng tiếng Việt
        val message = "Đã đến lúc tưới nước cho $plantName!"

        // Hiển thị thông báo
        makePlantReminderNotification(
            message,
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val nameKey = "NAME"
    }
}
