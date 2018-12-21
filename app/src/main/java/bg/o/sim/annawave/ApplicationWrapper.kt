package bg.o.sim.annawave

import android.app.Application

class ApplicationWrapper : Application() {

    companion object {
        var accessibleInstance: ApplicationWrapper? = null
    }

    override fun onCreate() {
        super.onCreate()
        accessibleInstance = this
    }
}