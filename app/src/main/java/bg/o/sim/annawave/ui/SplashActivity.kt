package bg.o.sim.annawave.ui

import android.app.ActivityOptions
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.*
import android.widget.ImageView
import bg.o.sim.annawave.ApplicationWrapper
import bg.o.sim.annawave.R
import bg.o.sim.annawave.model.LoginPerson
import bg.o.sim.annawave.networking.BackendService
import bg.o.sim.annawave.networking.HttpAsyncTask
import bg.o.sim.annawave.networking.model.LoginParams
import bg.o.sim.annawave.storage.loggedInPerson
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var backendService: BackendService
    private var systemUiVisible: Boolean = false

    private class AppPrepTask internal constructor(
        context: BaseActivity,
        private val backendService: BackendService
    ) : AsyncTask<Unit, Unit, Unit>() {
        private val contextReference: WeakReference<BaseActivity> = WeakReference(context)

        override fun doInBackground(vararg params: Unit) {
            val task = HttpAsyncTask<LoginPerson> { response ->
                if (response.isSuccess) {
                    val loginData: LoginPerson = response.result!!
                    loggedInPerson = loginData
                } else {
                    contextReference.get()?.showHttpErrorMessage(response)
                }
            }

            val loginParams = LoginParams("sa".toByteArray(), "123".toByteArray())
            task.execute(backendService.login(loginParams))

            /* PLACEHOLDER IMPL */
            /* if any async prep is necessary on application start, it can be place 'ere */
//            Thread.sleep(3_001)
        }

        override fun onPostExecute(result: Unit) {
            val activity = contextReference.get()
            if (activity != null) {
                val logoView: ImageView = activity.findViewById(R.id.logo_splash_activity)
                val viewTransitionName = activity.getString(R.string.transition_name_logo)
                val activityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(contextReference.get(), logoView, viewTransitionName)

                activity.startActivity(DashboardActivity::class.java, activityOptions.toBundle())
                contextReference.get()?.finishAfterTransition()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ApplicationWrapper.networkingComponent.inject(this)

        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            systemUiVisible = visibility and SYSTEM_UI_FLAG_FULLSCREEN == 0
        }

        hideSystemUI()
        logo_splash_activity.setOnClickListener {
            if (systemUiVisible) hideSystemUI()
            else showSystemUI()
        }
    }

    override fun onResume() {
        super.onResume()
        // run prep task, doing any necessary data preparation and start next Activity.
        AppPrepTask(this, backendService).execute()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
