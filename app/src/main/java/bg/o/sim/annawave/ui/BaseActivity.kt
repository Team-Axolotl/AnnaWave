package bg.o.sim.annawave.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import bg.o.sim.annawave.networking.HttpCallResponse
import bg.o.sim.annawave.networking.RpcException
import com.google.android.material.snackbar.Snackbar

/**
 * Root [Activity][AppCompatActivity], which is to be extended by *every* Activity instance in the application.
 * Provides a place to implement any logic that is to be present in *all* Application activities!
 */
abstract class BaseActivity : AppCompatActivity() {

    enum class ToastDuration(val value: Int) {
        LONG(Toast.LENGTH_LONG), SHORT(Toast.LENGTH_SHORT)
    }

    /** Show a toast with passed message and length ([Toast.LENGTH_LONG] by default) */
    @JvmOverloads
    fun toast(message: String, length: ToastDuration = ToastDuration.LONG) = Toast.makeText(this, message, length.value).show()

    fun toast(@StringRes messageResId: Int, length: ToastDuration = ToastDuration.LONG) {
        Toast.makeText(this, getString(messageResId), length.value).show()
    }

    /** Show error ui elements */
    fun showErrorMessage(message: String) {
        // todo - impl proper
        val snek: Snackbar = Snackbar.make(window.decorView, message, Snackbar.LENGTH_INDEFINITE)
        snek.setAction(android.R.string.ok) { snek.dismiss() }.show()
    }

    fun showHttpErrorMessage(response: HttpCallResponse<out Any>) {
        if( response.exception is RpcException)
            showErrorMessage(response.exception.message!!)
        else
            showErrorMessage(getString(response.errorCode?.messageStringId!!))
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle? = null) {
        this.startActivity(Intent(this, clazz), bundle)
    }
}