package com.paparazziteam.yakulap.presentation.login.views

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.hideKeyboardActivity
import com.paparazziteam.yakulab.binding.helper.isConnected
import com.paparazziteam.yakulab.binding.helper.isValidEmail
import com.paparazziteam.yakulab.binding.helper.setColorToStatusBar
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityLoginBinding
import com.paparazziteam.yakulap.presentation.dashboard.views.DashboardActivity
import com.paparazziteam.yakulap.presentation.login.viewmodels.ViewModelLogin
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var mPreferences: MyPreferences

    lateinit var txtRegistroNuevo: MaterialTextView
    var btnLoginEmail: MaterialButton? = null
    var isValidEmail = false
    var isValidPass:Boolean = false
    var _viewModelLogin = ViewModelLogin.getInstance()
    var TAG = this.javaClass.name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorToStatusBar(this)

        binding.apply {
            txtTermAndConditions.text = String.format(getString(R.string.terms_conditions_app_register), getString(R.string.app_name));
            txtRegistroNuevo = btnRegister
            btnLoginEmail    = binding.ingresarLoginButton
        }

        //Validate Data
        validateFields()
        openNewRegistro()

        //Observables with MVVM
        showObservables()

        //Login Firebase
        loginFirebase()
    }

    private fun showObservables() {
        _viewModelLogin.showMessage().observe(this) { message ->
            if (message != null) {
                _showMessageMainThread(message)
            }
        }
        _viewModelLogin.getIsLoginAnonymous().observe(this) { isLoginAnonymous ->
            if (isLoginAnonymous) {
                startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        //Login with email
        _viewModelLogin.getIsLoginEmail().observe(this) { isLoginEmail ->
            println("isLoginEmail: $isLoginEmail")
            if (isLoginEmail) {
                //Log.e(TAG, "EMAIL ENVIADO: " + binding.email.text.toString().lowercase())
                mPreferences.email = binding.email.text.toString().trim().lowercase()
                startActivity(
                    Intent(this, DashboardActivity::class.java)
                        .putExtra("email", binding.email.text.toString().lowercase())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
        _viewModelLogin.getIsLoading().observe(this) { isLoading ->
            Log.e("ISLOADING", "ISLOADING:$isLoading")
            if (isLoading) {
                binding.cortinaLayout.visibility = View.VISIBLE
            } else {
                binding.cortinaLayout.visibility = View.GONE
            }
        }
    }

    private fun loginFirebase() {
        btnLoginEmail!!.setOnClickListener {
            hideKeyboardActivity(this@LoginActivity)
            if (isConnected(applicationContext)) {
                _viewModelLogin.loginWithEmail(
                    binding.email.text.toString().trim(),
                    binding.pass.text.toString().trim()
                )
            } else _showMessageMainThread("Sin conexion a internet")
        }
    }

    private fun validateFields() {
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) validEmail(s.toString().trim { it <= ' ' })
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 5) {
                    binding.passLayout.error = null
                    isValidPass = true
                } else {
                    binding.passLayout.error = "La contraseña debe tener minimo 6 caracteres"
                    isValidPass = false
                }
                validEmailPass()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun validEmail(s: CharSequence) {
        if (isValidEmail(s)) {
            binding.emailLayout.error = null
            isValidEmail = true
        } else {
            binding.emailLayout.error = "Correo electrónico invalido"
            isValidEmail = false
        }
        validEmailPass()
    }


    private fun validEmailPass() {
        if (isValidEmail && isValidPass) {
            binding.ingresarLoginButton.apply {
                isEnabled = true
                backgroundTintMode = PorterDuff.Mode.SCREEN
                backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.colorPrimaryYaku)
                setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            }

        } else {
            binding.ingresarLoginButton.apply {
                isEnabled = false
                backgroundTintMode = PorterDuff.Mode.MULTIPLY
                backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.color_input_text)
                setTextColor(ContextCompat.getColor(applicationContext, R.color.color_input_text))
            }
        }
    }

    private fun openNewRegistro() {
        txtRegistroNuevo.apply {
            setOnClickListener {
                startActivity(Intent(this@LoginActivity,RegisterUserActivity::class.java))
            }
        }
    }

    private fun _showMessageMainThread(message: String) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        ViewModelLogin.destroyInstance()
        super.onDestroy()
    }
}