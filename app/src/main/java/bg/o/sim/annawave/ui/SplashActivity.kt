package bg.o.sim.annawave.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import android.widget.ImageView
import bg.o.sim.annawave.R
import bg.o.sim.annawave.model.LoginPerson
import bg.o.sim.annawave.networking.BACKEND_SERVICE
import bg.o.sim.annawave.networking.BackendService
import bg.o.sim.annawave.networking.HttpAsyncTask
import bg.o.sim.annawave.storage.loggedInPerson
import com.softwaregroup.underthekotlintree.util.showHttpErrorMessage
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.ref.WeakReference

class SplashActivity : AppCompatActivity() {
    private var systemUiVisible: Boolean = false

    private class AppPrepTask internal constructor(context: Activity) : AsyncTask<Unit, Unit, Unit>() {
        private val contextReference: WeakReference<Activity> = WeakReference(context)

        override fun doInBackground(vararg params: Unit) {
            val task = HttpAsyncTask<LoginPerson> { response ->
                if (response.isSuccess) {
                    val loginData: LoginPerson = response.result!!
                    loggedInPerson = loginData
                } else {
                    contextReference.get()?.showHttpErrorMessage(response)
                }
            }

            task.execute(BACKEND_SERVICE.login())

            /* PLACEHOLDER IMPL */
            /* if any async prep is necessary on application start, it can be place 'ere */
//            Thread.sleep(3_001)
        }

        override fun onPostExecute(result: Unit) {
            val activity = contextReference.get()
            if (activity != null){
                val logoView: ImageView = activity.findViewById(R.id.logo_splash_activity)
                val viewTransitionName = activity.getString(R.string.transition_name_logo)
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(contextReference.get(), logoView, viewTransitionName)

                val intent = Intent(activity, DashboardActivity::class.java)

                activity.startActivity(intent, activityOptions.toBundle())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
        AppPrepTask(this).execute()
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
