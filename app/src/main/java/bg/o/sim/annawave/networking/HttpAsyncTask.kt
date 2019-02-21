package bg.o.sim.annawave.networking

import android.os.AsyncTask
import android.util.Log
import bg.o.sim.annawave.storage.HttpErrorCode
import retrofit2.Call
import retrofit2.Response
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

/**
 * [AsyncTask] implementation for making http requests on a separate thread.
 *  The <T> generic type is used to determine the result's Type.
 *
 * @param onResult callback function which executes [onPostExecute]. Used to receive the result of the HttpAsyncTask.
 */
class HttpAsyncTask<T>(private inline val onResult: (HttpCallResponse<T>) -> Unit) : AsyncTask<Call<T>, Unit, HttpCallResponse<T>>() {

    override fun doInBackground(vararg calls: Call<T>): HttpCallResponse<T> {
        val call = calls[0]
        var response: Response<T>? = null

        return try {
            response = call.execute()
            HttpCallResponse.success(response.body()!!, call)
        } catch (ex: Throwable){
            val errorCode = when(ex){
                is RpcException -> HttpErrorCode.RPC_FAIL
                is ConnectException -> HttpErrorCode.CONNECT_FAIL
                is SocketTimeoutException -> HttpErrorCode.SOCKET_TIMEOUT
                is NoRouteToHostException -> HttpErrorCode.NO_ROUT
                is KotlinNullPointerException -> {
                    if (response!!.code() == HttpErrorCode.BAD_REQUEST.code) HttpErrorCode.BAD_REQUEST
                    else throw ex
                }
                // TODO FIXME STOPSHIP - temp until fix Socket T-Os
                else -> {
                    Log.e("fata", "FML: ", ex)
                    HttpErrorCode.CONNECT_FAIL
                }

//                else -> throw ex
            }

            HttpCallResponse.error(errorCode, ex, call)
        }
    }

    override fun onPostExecute(result: HttpCallResponse<T>) {
        onResult(result)
    }
}