package bg.o.sim.annawave.to_debug_a_mocking_bird.injection.modules

import bg.o.sim.annawave.injection.modules.NetworkingModule
import bg.o.sim.annawave.model.Client
import bg.o.sim.annawave.model.LoginPerson
import bg.o.sim.annawave.model.Task
import bg.o.sim.annawave.model.TaskType
import bg.o.sim.annawave.networking.BackendService
import bg.o.sim.annawave.networking.JsonRpcError
import bg.o.sim.annawave.networking.RpcException
import bg.o.sim.annawave.networking.model.LoginParams
import bg.o.sim.annawave.storage.NetworkingConfig
import dagger.Module
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Converter
import retrofit2.mock.Calls


@Module
class MockNetworkingModule(networkingConfig: NetworkingConfig) : NetworkingModule(networkingConfig) {
    override fun getBackendService(httpClient: OkHttpClient, converterFactory: Converter.Factory): BackendService {
        return MockBackendService()
    }
}

private class MockBackendService : BackendService {

    val tskSts = arrayOf("done", "canceled", "pending")
    val clntNms = arrayOf("Sumiko","Bret","Gita","Edda","Kathie","Malcom","Ozell","Jeffery","Ross","Evelina","Vergie","Lory","Dominque","Naomi","Junie","Phung","Nyla","Jesusita","Tequila","Clarisa","Dorie","Tana","Enriqueta","Joyce","Zena","Maritza","Cindie","Lynwood","Georgeann","Lionel","Brooks","Maryrose","Lorilee","Wilfred","Janis","Delicia","Ivette","Deandrea","Lindsay","Caryn","Jalisa","Catina","Leta","Judith","Federico","Nieves","Trevor","Janine","Kyong","Rachel")
    fun phnNmbr(): String = "088" + repeat((0 .. 7).count()) { (0 .. 9).random() }

    override fun login(loginParams: LoginParams): Call<LoginPerson> {
        val uName = loginParams.userName
        val passwd = loginParams.password
        if (uName.contentEquals("sa".toByteArray()) && passwd.contentEquals("123".toByteArray())){
            val loginPerson = LoginPerson(
                (0 .. 999).random(),
                (0 .. 999).random(),
                (0 .. 999).random(),
                arrayOf(
                    Task(tskSts.random(),TaskType.values().random(), Client(clntNms.random(), clntNms.random(), phnNmbr()))
                )
            )
            return Calls.response(loginPerson)
        } else {
            return Calls.failure(
                RpcException(
                    JsonRpcError(
                        "404",
                        "Mock failure: Invalid credentials ... or something",
                        "Mock failure: Invalid credentials ... or something",
                        "Mock type: terror error"
                    )
                )
            )
        }
    }
}

