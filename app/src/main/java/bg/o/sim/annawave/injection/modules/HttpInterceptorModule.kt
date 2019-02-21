package bg.o.sim.annawave.injection.modules

import android.util.Log
import bg.o.sim.annawave.storage.HTTP_LOG_TAG
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class HttpInterceptorModule {

    @Provides
    @Singleton
    fun getHttpInterceptors(): Array<Interceptor> {
        return arrayOf(
            AuthInterceptor,
            HttpLoggingInterceptor { Log.d(HTTP_LOG_TAG, "\n $it \n") }.setLevel(HttpLoggingInterceptor.Level.BODY)
        )
    }
}

/** Request interceptor for injecting the jwt/xsrf cookie once logged-in */
object AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain!! // assert that [chain] is non-null

//        // TODO - clean-up dem old codeses or adapt to $CURRENT_ARCH
//        //if jwt and/or xsrf are not initialized -> DOUN'T TOUCH MAH COOKI! *rabble-rabble*
//        if (jwt == null || xsrf == null) return chain.proceed(chain.request())
//
//        // get old cookie or empty string (to avoid null values)
//        var cookie: String = chain.request().header("Cookie") ?: ""
//
//        // if the old one is empty -> replace it with [getCooke]
//        if (cookie.isBlank()) cookie = getCookie()
//        val request = chain.request().newBuilder()
//        val request = chain.request().newBuilder()
//            .header("Cookie", cookie)
//            .build()
//
//        return chain.proceed(request)

        return chain.proceed(chain.request().newBuilder().build())
    }
}