// SPDX-FileCopyrightText: 2017-2023 Alexey Rochev <equeim@gmail.com>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package org.equeim.tremotesf.rpc

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.MainThread
import androidx.work.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.equeim.tremotesf.TremotesfApplication
import org.equeim.tremotesf.torrentfile.rpc.Servers
import org.equeim.tremotesf.torrentfile.rpc.WifiNetworkServersController
import org.equeim.tremotesf.ui.AppForegroundTracker
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference


@SuppressLint("StaticFieldLeak")
object GlobalServers : Servers(@OptIn(DelicateCoroutinesApi::class) GlobalScope, TremotesfApplication.instance) {
    val wifiNetworkController = WifiNetworkServersController(this, AppForegroundTracker.appInForeground, scope, context)

    private val saveData = AtomicReference<ServersState>()

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Main.immediate) {
            wifiNetworkController.setCurrentServerFromWifiNetwork()
        }
    }

    @MainThread
    override fun save(serversState: ServersState) {
        this.saveData.set(serversState)
        WorkManager.getInstance(context).enqueueUniqueWork(
            SaveWorker.UNIQUE_WORK_NAME,
            ExistingWorkPolicy.APPEND,
            OneTimeWorkRequestBuilder<SaveWorker>().build()
        )
    }

    class SaveWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters) {

        override fun doWork(): Result {
            Timber.i("doWork() called")
            val serversState = saveData.getAndSet(null)
            if (serversState != null) {
                doSave(serversState)
            } else {
                Timber.w("doWork: SaveData is null")
            }
            return Result.success()
        }

        override fun onStopped() {
            Timber.i("onStopped() called")
            saveData.set(null)
        }

        companion object {
            const val UNIQUE_WORK_NAME = "ServersSaveWorker"
        }
    }
}
