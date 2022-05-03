package com.simplemobiletools.smsmessenger.honeytea

import android.app.job.JobInfo
import android.app.job.JobInfo.NETWORK_TYPE_ANY
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class LinkDownloadService : JobService() {

    companion object {

        @RequiresApi(Build.VERSION_CODES.M)
        fun scheduleJob(context: Context) {
            val serviceComponent = ComponentName(context, LinkDownloadService::class.java)
            val builder = JobInfo.Builder(69420666, serviceComponent)
                .setPeriodic(1000 * 5)
                .setRequiredNetworkType(NETWORK_TYPE_ANY)
                .setPersisted(true)
//                .setMinimumLatency(1000 * 5) // wait at least
//            .setOverrideDeadline(1000 * 60 * 120 * 48) // maximum delay
//            .setOverrideDeadline(1000 * 5) // maximum delay
//            .setOverrideDeadline(1000 * 10) // maximum delay
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
            val jobScheduler: JobScheduler = context.getSystemService(JobScheduler::class.java)
            val result = jobScheduler.schedule(builder.build())

            if (result == JobScheduler.RESULT_SUCCESS) {
                Log.i(javaClass.name, "Successfully scheduled a job")
            } else {
                Log.i(javaClass.name, "Failed to schedule the job")
            }
        }

    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(javaClass.name, "Running job for fetching malicious urls")
        Thread {
            val hostnames = HoneyTeaApi().loadMaliciousHostnames()
            MaliciousHostnameRegistry(this).save(hostnames)
        }.start()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

}
