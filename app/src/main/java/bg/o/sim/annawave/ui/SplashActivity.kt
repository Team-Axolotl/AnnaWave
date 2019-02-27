package bg.o.sim.annawave.ui

import android.app.ActivityOptions
import android.os.AsyncTask
import android.os.Bundle
import android.view.View.*
import android.widget.ImageView
import bg.o.sim.annawave.ApplicationWrapper
import bg.o.sim.annawave.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.ref.WeakReference

class SplashActivity : BaseActivity() {

    private var systemUiVisible: Boolean = false

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

    override fun onStart() {
        super.onStart()
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


    private class AppPrepTask internal constructor(
        context: BaseActivity
    ) : AsyncTask<Unit, Unit, Unit>() {
        private val contextReference: WeakReference<BaseActivity> = WeakReference(context)

        override fun doInBackground(vararg params: Unit) {
            /* PLACEHOLDER IMPL */
            /* if any async prep is necessary on application start, it can be place 'ere */
            Thread.sleep(1172)
        }

        override fun onPostExecute(result: Unit) {
            val activity = contextReference.get()
            if (activity != null) {
                val logoView: ImageView = activity.findViewById(R.id.logo_splash_activity)
                val viewTransitionName = activity.getString(R.string.transition_name_logo)
                val activityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(contextReference.get(), logoView, viewTransitionName)

                activity.startActivity(LoginActivity::class.java, activityOptions.toBundle())
                contextReference.get()?.finishAfterTransition()
            }
        }
    }
}
