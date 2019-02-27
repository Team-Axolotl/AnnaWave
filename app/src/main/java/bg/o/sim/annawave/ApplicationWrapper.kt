package bg.o.sim.annawave

import android.app.Application
import bg.o.sim.annawave.injection.modules.ApplicationModule
import bg.o.sim.annawave.injection.modules.HttpInterceptorModule
import bg.o.sim.annawave.injection.modules.NetworkingModule
import bg.o.sim.annawave.storage.NetworkingConfig
import bg.o.sim.annawave.ui.BaseActivity
import bg.o.sim.annawave.ui.LoginActivity
import dagger.Component
import javax.inject.Singleton


class ApplicationWrapper : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
            private set

        lateinit var networkingComponent: NetworkingComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // TODO - temp - depInject dev only instantiation
        val networkingConfig = NetworkingConfig(
            baseUrl = "0.0.0.0",
            port = 8,
            useHttpCache = false
        )

        // Dagger%COMPONENT_NAME%
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        networkingComponent = DaggerNetworkingComponent.builder()
            .application(this)
            .httpInterceptorModule(HttpInterceptorModule())
            .networkingModule(NetworkingModule(networkingConfig))
            .build()
    }
}

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    /**
     * base classes are not sufficient as injection targets.
     * Dagger 2 relies on strongly typed classes, so you must specify explicitly which ones should be defined.
     */
    fun inject(app: BaseActivity)
}

@Singleton
@Component(modules = [NetworkingModule::class, HttpInterceptorModule::class], dependencies = [Application::class])
interface NetworkingComponent {
    fun inject(activity: LoginActivity)
}