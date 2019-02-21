package bg.o.sim.annawave.networking

import bg.o.sim.annawave.model.LoginPerson
import retrofit2.Call
import retrofit2.http.GET

// REQUEST METHODS
const val REQUEST_IDENTITY_CHECK = "identity.check"
const val REQUEST_USER_USER_FETCH = "user.user.fetch"
const val REQUEST_USER_USER_GET = "user.user.get"
// END REQUEST METHODS

interface BackendService {
    @GET("/login/get/5c1c736122201b628cf44c9d")
    fun login(): Call<LoginPerson>
}

data class JsonRpcResponse<out T>(
    val id: Int,
    val jsonrpc: String,
    val result: T?,
    val error: JsonRpcError?
)

data class JsonRpcError(
    val code: String,
    val message: String,
    val errorPrint: String?,
    val type: String
)

data class JsonRpcRequest(
    val id: Int = 1, // TODO - track id RPC request-repose №
    val jsonrpc: String = "2.0",
    val method: String,
    val params: Map<String, Any>
)