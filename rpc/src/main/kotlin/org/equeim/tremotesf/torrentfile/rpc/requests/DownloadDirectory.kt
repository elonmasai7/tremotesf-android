package org.equeim.tremotesf.torrentfile.rpc.requests

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.equeim.tremotesf.torrentfile.rpc.RpcClient
import org.equeim.tremotesf.torrentfile.rpc.RpcRequestError

/**
 * @return Default download directory
 * @throws RpcRequestError
 */
suspend fun RpcClient.getDownloadDirectory(): NormalizedRpcPath {
    return performRequest<RpcResponse<DownloadDirectoryResponseArguments>>(DOWNLOAD_DIRECTORY_REQUEST, "getDownloadDirectory")
        .arguments
        .downloadDirectory
}

@Serializable
private data class DownloadDirectoryRequestArguments(
    @SerialName("fields")
    val fields: List<String> = listOf("download-dir"),
)

private val DOWNLOAD_DIRECTORY_REQUEST = RpcRequestBody(RpcMethod.SessionGet, DownloadDirectoryRequestArguments())

@Serializable
private data class DownloadDirectoryResponseArguments(
    @Contextual
    @SerialName("download-dir")
    val downloadDirectory: NormalizedRpcPath,
)
