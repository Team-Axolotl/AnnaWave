package bg.o.sim.annawave.injection.modules

import android.app.Application
import bg.o.sim.annawave.storage.NetworkingConfig
import bg.o.sim.annawave.networking.BackendService
import bg.o.sim.annawave.networking.JacksonObjMapper
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class NetworkingModule(private val networkingConfig: NetworkingConfig) {

    @Provides
    @Singleton
    fun getHttpCache(application: Application): Cache? {
        return if (networkingConfig.useHttpCache) {
            Cache(application.cacheDir, networkingConfig.cacheSizeBytes)
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun getConverterFactory(): Converter.Factory = JacksonConverterFactory.create(JacksonObjMapper)

    @Provides
    @Singleton
    fun getBackendClient(httpCache: Cache?, interceptors: Array<Interceptor>): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(networkingConfig.callTimeoutMillisec, TimeUnit.MILLISECONDS)
            .readTimeout(networkingConfig.readTimeoutMillisec, TimeUnit.MILLISECONDS)
            .callTimeout(networkingConfig.callTimeoutMillisec, TimeUnit.MILLISECONDS)
            .writeTimeout(networkingConfig.writeTimeoutMillisec, TimeUnit.MILLISECONDS)
            .cache(httpCache)

        interceptors.forEach {
            clientBuilder.addInterceptor(it)
        }

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    open fun getBackendService(httpClient: OkHttpClient, converterFactory: Converter.Factory): BackendService {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(networkingConfig.getUrl())
            .addConverterFactory(converterFactory)
            .build()
            .create(BackendService::class.java)
    }
}