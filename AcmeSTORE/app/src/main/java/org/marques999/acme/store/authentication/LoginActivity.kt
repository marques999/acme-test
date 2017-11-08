package org.marques999.acme.store.authentication

import android.os.Bundle
import android.content.Intent
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity

import org.marques999.acme.store.common.AcmeDialogs
import org.marques999.acme.store.common.Authentication
import org.marques999.acme.store.common.HttpErrorHandler
import org.marques999.acme.store.common.SessionJwt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

import org.marques999.acme.store.AcmeStore
import org.marques999.acme.store.MainActivity
import org.marques999.acme.store.R

import kotlinx.android.synthetic.main.activity_login.*

import org.marques999.acme.store.api.AuthenticationProvider
import org.marques999.acme.store.authentication.RegisterConstants.generateError

class LoginActivity : AppCompatActivity() {

    /**
     */
    private lateinit var progressDialog: ProgressDialog

    /**
     */
    private fun authenticate(
        username: String,
        password: String
    ) = AuthenticationProvider().login(
        Authentication(username, password)
    ).observeOn(
        AndroidSchedulers.mainThread()
    ).subscribeOn(
        Schedulers.io()
    ).subscribe(
        onLoginCompleted(username),
        onLoginFailed(HttpErrorHandler(this))
    )

    /**
     */
    private fun onLoginCompleted(username: String) = Consumer<SessionJwt> {

        progressDialog.dismiss()
        loginActivity_login.isEnabled = true
        (application as AcmeStore).initializeApi(username, it)

        Intent(this, MainActivity::class.java).let {
            startActivity(it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    /**
     */
    private fun onLoginFailed(next: Consumer<Throwable>) = Consumer<Throwable> {
        progressDialog.dismiss()
        loginActivity_login.isEnabled = true
        next.accept(it)
    }

    /**
     */
    private fun authenticateCustomer() {
        loginActivity_login.isEnabled = false
        progressDialog.show()
        authenticate(loginActivity_username.text.toString(), loginActivity_password.text.toString())
    }

    /**
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null &&
            resultCode == AppCompatActivity.RESULT_OK &&
            requestCode == AcmeStore.REQUEST_REGISTER) {
            loginActivity_username.setText(data.getStringExtra(RegisterConstants.EXTRA_USERNAME))
            loginActivity_password.setText(data.getStringExtra(RegisterConstants.EXTRA_PASSWORD))
        }
    }

    /**
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = AcmeDialogs.buildProgress(
            this,
            R.string.loginActivity_progress
        )

        loginActivity_register.setOnClickListener {

            Intent(applicationContext, RegisterActivity::class.java).let {
                startActivityForResult(
                    it,
                    AcmeStore.REQUEST_REGISTER
                )
            }
        }

        loginActivity_login.setOnClickListener {

            if (validateForm()) {
                authenticateCustomer()
            }
        }

        (application as AcmeStore).loadCustomer().let {
            loginActivity_username.setText(it.username)
            loginActivity_password.setText(it.password)
        }
    }

    /**
     */
    private fun validateForm(): Boolean {

        var formValid = true
        val username = loginActivity_username.text.toString()

        when {
            username.isEmpty() -> {
                loginActivity_username.error = generateError(R.string.errorRequired)
                formValid = false
            }
            RegisterConstants.invalidUsername(username) -> {
                loginActivity_username.error = generateError(R.string.errorUsername)
                formValid = false
            }
            else -> {
                loginActivity_username.error = null
            }
        }

        val password = loginActivity_password.text.toString()

        when {
            password.isEmpty() -> {
                loginActivity_password.error = generateError(R.string.errorRequired)
                formValid = false
            }
            RegisterConstants.invalidPassword(password) -> {
                loginActivity_password.error = generateError(R.string.errorPassword)
                formValid = false
            }
            else -> {
                loginActivity_password.error = null
            }
        }

        return formValid
    }
}