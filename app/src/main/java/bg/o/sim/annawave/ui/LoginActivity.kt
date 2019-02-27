package bg.o.sim.annawave.ui

import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
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
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class LoginActivity : BaseActivity() {

    @Inject
    lateinit var backendService: BackendService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ApplicationWrapper.networkingComponent.inject(this)
        button_login.setOnClickListener {
            hideKeyboard()
            if (validateInput()) {
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

    private fun validateInput(): Boolean {
        // TODO - remove magic numbers @ length
        // TODO - remove magic strings
        // TODO - once config is implemented, add check for attempt-count lock.

        if (input_user_name.text!!.length < 2) {
            setError("Please enter a valid user id!")
            return false
        }

        if (input_password.text!!.length < 3) {
            setError("Please enter a valid password!")
            return false
        }

        clearError()
        return true
    }



    private fun setError(error: String) {
        input_user_name.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.redWarning))
        input_password.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.redWarning))

        error_modal.visibility = View.VISIBLE
        error_modal.text = error

        hideKeyboard()
    }

    private fun clearError() {
        input_user_name.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        input_password.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        error_modal.visibility = View.GONE
        error_modal.text = ""
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
            Thread.sleep(2_001)
        }

        private fun openDashboardActivity() {
            //TODO - move to Activity and add a callback
            val activity = contextReference.get()!! // assert underlying activity hasn't been garbage collected
            activity.startActivity(DashboardActivity::class.java, null)
        }
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}