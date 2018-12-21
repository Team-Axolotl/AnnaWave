package bg.o.sim.annawave.networking

import bg.o.sim.annawave.model.LoginPerson
import bg.o.sim.annawave.storage.baseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
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

var BACKEND_SERVICE: BackendService = createBackendService()

fun createBackendService(): BackendService = Retrofit.Builder()
        .client(BACKEND_CLIENT)
        .baseUrl(baseUrl)
        .addConverterFactory(
            WrappedJacksonConverterFactory(
                JacksonConverterFactory.create(
                    JacksonObjMapper
                )
            )
        )
        .build()
        .create(BackendService::class.java)

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
        val id: Int = 1,
        val jsonrpc: String = "2.0",
        val method: String,
        val params: Map<String, Any>
)