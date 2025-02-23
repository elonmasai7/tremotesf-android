// SPDX-FileCopyrightText: 2017-2023 Alexey Rochev <equeim@gmail.com>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package org.equeim.tremotesf.torrentfile.rpc.requests.serversettings

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer
import org.equeim.tremotesf.torrentfile.rpc.RpcClient
import org.equeim.tremotesf.torrentfile.rpc.RpcRequestError
import org.equeim.tremotesf.torrentfile.rpc.requests.RpcMethod
import org.equeim.tremotesf.torrentfile.rpc.requests.RpcResponseWithoutArguments

/**
 * @throws RpcRequestError
 */
internal suspend inline fun <reified T> RpcClient.setSessionProperty(
    property: String,
    value: T,
): Unit =
    setSessionProperty(property, value, serializer())

/**
 * @throws RpcRequestError
 */
internal suspend fun <T> RpcClient.setSessionProperty(
    property: String,
    value: T,
    serializer: KSerializer<T>,
) {
    performRequest<RpcResponseWithoutArguments, _>(
        RpcMethod.SessionSet,
        buildJsonObject { put(property, json.encodeToJsonElement(serializer, value)) },
        property
    )
}
