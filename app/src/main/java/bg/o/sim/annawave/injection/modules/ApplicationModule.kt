package bg.o.sim.annawave.injection.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val appInstance: Application) {

    @Provides
    @Singleton
    fun getApplication() : Application = appInstance
}