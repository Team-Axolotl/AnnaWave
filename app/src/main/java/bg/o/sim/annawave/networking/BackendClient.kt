package bg.o.sim.annawave.networking

import android.util.Log
import bg.o.sim.annawave.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

const val HTTP_LOG = "HttpLog"

/** Representation of a http error mapped to a strings.xml value for human-readable message. */
enum class ErrorCode(val code: Int, val messageStringId: Int) {
    SOCKET_TIMEOUT(408, R.string.error_message_connection_time_out),
    CONNECT_FAIL(418, R.string.error_message_no_connection),
    BAD_REQUEST(400, R.string.error_message_bad_request),
    NO_ROUT(400, R.string.error_message_no_rout_to_host),
    RPC_FAIL(200, R.string.error_message_rpc_error)
}

/** Client configuration for the [BACKEND_SERVICE] */
val BACKEND_CLIENT: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(30_000, TimeUnit.MILLISECONDS)
    .readTimeout(30_000, TimeUnit.MILLISECONDS)
    .callTimeout(30_000, TimeUnit.MILLISECONDS)
    .writeTimeout(30_000, TimeUnit.MILLISECONDS)
    .addInterceptor(HttpLoggingInterceptor { Log.d(HTTP_LOG, "\n $it \n") }.setLevel(HttpLoggingInterceptor.Level.BODY))
    .addInterceptor(AuthInterceptor())
    .build()

/** Request interceptor for injecting the jwt/xsrf cookie once logged-in */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain!! // assert that [chain] is non-null

        // TODO - clean-up dem old codeses
//        //if jwt and/or xsrf are not initialized -> DOUN'T TOUCH MAH COOKI! *rabble-rabble*
//        if (jwt == null || xsrf == null) return chain.proceed(chain.request())
//
//        // get old cookie or empty string (to avoid null values)
//        var cookie: String = chain.request()?.header("Cookie") ?: ""
//
//        // if the old one is empty -> replace it with [getCooke]
//        if (cookie.isBlank()) cookie = getCookie()
        val request = chain.request().newBuilder()
//                .header("Cookie", cookie)
            .build()

        return chain.proceed(request)
    }
}