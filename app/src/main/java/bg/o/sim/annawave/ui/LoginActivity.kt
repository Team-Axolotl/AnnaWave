package bg.o.sim.annawave.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import bg.o.sim.annawave.ApplicationWrapper
import bg.o.sim.annawave.R
import bg.o.sim.annawave.model.LoginPerson
import bg.o.sim.annawave.networking.BackendService
import bg.o.sim.annawave.networking.HttpAsyncTask
import bg.o.sim.annawave.networking.model.LoginParams
import bg.o.sim.annawave.storage.loggedInPerson
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var backendService: BackendService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ApplicationWrapper.networkingComponent.inject(this)
        button_login.setOnClickListener {
            if (validInput()) {
                toast("[TODO - move to dialog] Logging in.") // TODO - move to dialog
                LoginTask(this, backendService).execute()
            }
        }

        input_password.imeOptions = EditorInfo.IME_ACTION_DONE
        input_password.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                button_login.callOnClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun validInput(): Boolean {
        // TODO - remove magi cnumbers @ length
        var valid = true

        if (input_user_name.text!!.length < 2) {
            valid = false
            input_user_name.error = "Please enter a valid user id!"
        } else {
            input_user_name.error = null
        }

        if (input_password.text!!.length < 3) {
            valid = false
            input_password.error = "Please enter your password!"
        } else {
            input_password.error = null
        }

        return valid
    }


    private fun getPassword(): ByteArray = input_password.text.toString().toByteArray()

    private fun getUserName(): ByteArray = input_user_name.text.toString().toByteArray()


    private class LoginTask internal constructor(
        context: LoginActivity,
        private val backendService: BackendService
    ) : AsyncTask<Unit, Unit, Unit>() {
        private val contextReference: WeakReference<LoginActivity> = WeakReference(context)

        lateinit var loginParams: LoginParams

        override fun onPreExecute() {
            super.onPreExecute()
            val activity = contextReference.get()!! // assert underlying activity hasn't been garbage collected

            if (activity.isFinishing || activity.isDestroyed) {
                this.cancel(true)
                return
            }

            loginParams = LoginParams(activity.getUserName(), activity.getPassword())
        }

        override fun doInBackground(vararg params: Unit) {
            if (isCancelled) return

            val task = HttpAsyncTask<LoginPerson> { response ->
                if (!isCancelled) {
                    if (response.isSuccess) {
                        val loginData: LoginPerson = response.result!!
                        loggedInPerson = loginData
                        openDashboardActivity()
                    } else {
                        contextReference.get()?.showHttpErrorMessage(response)
                    }
                }
            }

            task.execute(backendService.login(loginParams))

            /* PLACEHOLDER IMPL */
            /* if any async prep is necessary on application start, it can be place 'ere */
            Thread.sleep(3_001)
        }

        private fun openDashboardActivity() {
            //TODO - move to Activity and add a callback
            val activity = contextReference.get()!! // assert underlying activity hasn't been garbage collected
            activity.startActivity(DashboardActivity::class.java, null)
        }
    }
}