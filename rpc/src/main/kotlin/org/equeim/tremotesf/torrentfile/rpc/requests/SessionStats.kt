// SPDX-FileCopyrightText: 2017-2023 Alexey Rochev <equeim@gmail.com>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package org.equeim.tremotesf.torrentfile.rpc.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.equeim.tremotesf.torrentfile.rpc.RpcClient
import org.equeim.tremotesf.torrentfile.rpc.RpcRequestError
import kotlin.time.Duration

/**
 * @return Session stats
 * @throws RpcRequestError
 */
suspend fun RpcClient.getSessionStats(): SessionStatsResponseArguments =
    performRequest<RpcResponse<SessionStatsResponseArguments>>(SESSION_STATS_REQUEST).arguments

private val SESSION_STATS_REQUEST = RpcRequestBody(RpcRequestWithoutArguments(RpcMethod.SessionStats))

@Serializable
data class SessionStatsResponseArguments(
    @SerialName("downloadSpeed")
    val downloadSpeed: TransferRate,
    @SerialName("uploadSpeed")
    val uploadSpeed: TransferRate,
    @SerialName("current-stats")
    val currentSession: Stats,
    @SerialName("cumulative-stats")
    val total: Stats,
) {
    @Serializable
    data class Stats(
        @SerialName("downloadedBytes")
        val downloaded: FileSize,
        @SerialName("uploadedBytes")
        val uploaded: FileSize,
        @SerialName("sessionCount")
        val sessionCount: Int,
        @Serializable(SecondsToDurationSerializer::class)
        @SerialName("secondsActive")
        val active: Duration,
    )
}
