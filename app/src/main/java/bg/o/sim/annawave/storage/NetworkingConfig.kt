package bg.o.sim.annawave.storage

import bg.o.sim.annawave.R
import okhttp3.HttpUrl

private const val DEFAULT_TIMEOUT_CONNECT_MS: Long = 30 * 1000 //30 seconds
private const val DEFAULT_TIMEOUT_READ_MS: Long = 30 * 1000 //30 seconds
private const val DEFAULT_TIMEOUT_CALL_MS: Long = 30 * 1000 //30 seconds
private const val DEFAULT_TIMEOUT_WRITE_MS: Long = 30 * 1000 //30 seconds

/** If httpCache deactivated, make sure any call to [okhttp3.Cache] constructors will fail.
 * @see [okhttp3.internal.cache.DiskLruCache.create] will throw [IllegalArgumentException]*/                            //fail-fast - have a blast (¬‿¬)
private const val NO_CACHE: Long = -1
private const val DEFAULT_CACHE_SIZE_MIB: Long = 10 * 1024 * 1024 // 10 MiB

private const val HTTP = "http"
private const val HTTPS = "https"

private const val PORT_NOT_SET = -1

const val HTTP_LOG_TAG = "HttpLog"

/**
 * TODO - note required parameters!!
 * Represent the basic Network host configuration.
 *
 * Provides an [okhttp3.HttpUrl] via [getUrl] built using the configured params.
 * The [HttpUrl.Builder] validates all input and throws an [IllegalArgumentException] if any item is invalid
 * => build HttpUrl will always be structurally valid if no Exception was thrown.
 *
 * *If [port] is not set, OkHttp3 internally defaults to [okhttp3.HttpUrl.defaultPort]*
 * *[isHttps] defaults to true*
 */
data class NetworkingConfig(
    // host config
    val baseUrl: String,
    val port: Int = PORT_NOT_SET,
    val isHttps: Boolean = true,
    // time-out config
    val connectTimeoutMillisec: Long = DEFAULT_TIMEOUT_CONNECT_MS,
    val readTimeoutMillisec: Long = DEFAULT_TIMEOUT_READ_MS,
    val callTimeoutMillisec: Long = DEFAULT_TIMEOUT_CALL_MS,
    val writeTimeoutMillisec: Long = DEFAULT_TIMEOUT_WRITE_MS,
    // caching config
    val useHttpCache: Boolean = true,
    val cacheSizeBytes: Long = if (useHttpCache) DEFAULT_CACHE_SIZE_MIB else NO_CACHE
) {

    fun getUrl(): HttpUrl = HttpUrl.Builder()
        .scheme(if (isHttps) HTTPS else HTTP)
        .host(baseUrl)
        .port(port)
        .build()
}


/** Representation of a http error mapped to a strings.xml value for human-readable message. */
enum class HttpErrorCode(val code: Int, val messageStringId: Int) {
    SOCKET_TIMEOUT(408, R.string.error_message_connection_time_out),
    CONNECT_FAIL(418, R.string.error_message_no_connection),
    BAD_REQUEST(400, R.string.error_message_bad_request),
    NO_ROUT(400, R.string.error_message_no_rout_to_host),
    RPC_FAIL(200, R.string.error_message_rpc_error)
}
