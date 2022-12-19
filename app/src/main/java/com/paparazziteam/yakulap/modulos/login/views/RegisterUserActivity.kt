package com.paparazziteam.yakulap.modulos.login.views

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityRegisterUserBinding
import com.paparazziteam.yakulap.helper.hideKeyboardActivity
import com.paparazziteam.yakulap.helper.isValidEmail
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.modulos.dashboard.views.DashboardActivity
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.viewmodels.ViewModelRegistroUsuario

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    val _viewModel = ViewModelRegistroUsuario.getInstance()

    var edtFullname: TextInputEditText? = null
    var edtLastname:TextInputEditText? = null
    var edtEmail:TextInputEditText? = null
    var edtAlias:TextInputEditText? = null
    var edtPass:TextInputEditText? = null
    var inputLayoutFullname: TextInputLayout? = null
    var inputLayoutLastname:TextInputLayout? = null
    var inputLayoutAlias:TextInputLayout? = null
    var inputLayoutEmail:TextInputLayout? = null
    var inputLayoutPass:TextInputLayout? = null

    var isValidFullname = false
    var isValidlastname:Boolean? = false
    var isValidAlias:Boolean? = false
    var isValidEmail:Boolean? = false
    var isValidPass:Boolean? = false
    var btnSignUp: MaterialButton? = null

    private var toolbar  : Toolbar ? = null

    //Sign up
    var userNew = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorToStatusBar(this)

        binding.apply {
            toolbar = tool.toolbar
            termsConditions.text = getString(R.string.terms_conditions_app_register, getString(R.string.app_name))
            edtFullname          = fullname
            edtLastname         = lastname
            edtEmail            = email
            edtAlias            = alias
            edtPass             = password
            inputLayoutFullname = layoutFullname
            inputLayoutLastname = layoutLastname
            inputLayoutEmail    = layoutEmail
            inputLayoutAlias    = layoutAlias
            inputLayoutPass     = layoutPass
            btnSignUp           = signUp
        }

        signUp()
        hideKeyboardActivity(this)
        validateFields()
        setupToolbar()
        observers()
    }

    private fun validateFields() {
        edtFullname!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 2) {
                    inputLayoutFullname!!.error = null
                    isValidFullname = true
                } else {
                    inputLayoutFullname!!.error = "El campo debe tener minimo 3 caracteres"
                    isValidPass = false
                }
                isAllValid()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edtLastname?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 2) {
                    inputLayoutLastname!!.error = null
                    isValidlastname = true
                } else {
                    inputLayoutLastname!!.error = "El campo debe tener minimo 3 caracteres"
                    isValidlastname = false
                }
                isAllValid()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edtAlias?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 2) {
                    inputLayoutAlias?.error = null
                    isValidAlias = true
                    //LLamar al metodo principal
                } else {
                    inputLayoutAlias?.error = "El campo debe tener minimo 3 caracteres"
                    isValidAlias = false
                }
                isAllValid()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edtEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValidEmail(s)) {
                    inputLayoutEmail!!.error = null
                    isValidEmail = true
                } else {
                    inputLayoutEmail!!.error = "Correo electrónico invalido"
                    isValidEmail = false
                }
                isAllValid()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edtPass!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 5) {
                    inputLayoutPass!!.error = null
                    isValidPass = true
                } else {
                    inputLayoutPass!!.error = "La contraseña debe tener minimo 6 caracteres"
                    isValidPass = false
                }
                isAllValid()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun isAllValid() {
        if (isValidAlias!!
            && isValidPass!!
            && isValidEmail!!
            && isValidFullname
            && isValidlastname!!
        ) {
            btnSignUp?.apply {
                isEnabled = true
                backgroundTintMode = PorterDuff.Mode.SCREEN
                backgroundTintList =
                    ContextCompat.getColorStateList(applicationContext, R.color.colorPrimaryYaku)
                setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            }
        } else {

            btnSignUp?.apply {
                isEnabled = false
                backgroundTintMode = PorterDuff.Mode.MULTIPLY
                backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.color_input_text)
                setTextColor(ContextCompat.getColor(applicationContext, R.color.color_input_text))
            }
        }

    }

    private fun observers() {

        _viewModel.showMessage().observe(this) { message ->
            if (message != null) {
                _showMessageMainThread(message)
            }
        }

        _viewModel.getUser().observe(this) { user ->
            if (userNew.email.equals(user.email)) {
                _saveOnFirebase(userNew)
            } else {
                _showMessageMainThread(user.email)
            }
        }

        _viewModel.getIsLoading().observe(this) { isLoading ->
            Log.e("ISLOADING", "ISLOADING:$isLoading")
            if (isLoading) {
                binding.cortinaLayout.visibility = View.VISIBLE
            } else {
                binding.cortinaLayout.visibility = View.GONE
            }
        }

        _viewModel.getIsSavedFirebase().observe(this) { isSavedFirebase ->
            if (isSavedFirebase) {
                goToPrincipal()
            } else {
                _showMessageMainThread("isSavedFirebase:" + isSavedFirebase.toString())
            }
        }


    }

    private fun _saveOnFirebase(user: User) {
        _viewModel.saveFirebaseUser(user)
    }

    private fun goToPrincipal() {
       val i = Intent(applicationContext, DashboardActivity::class.java)
            .putExtra("email", edtEmail!!.text.toString().trim())
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK //eliminar activities anteriores
        startActivity(i)
    }

    private fun _showMessageMainThread(message: String?) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun setupToolbar() {
        toolbar?.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryYaku))
            setNavigationOnClickListener { onBackPressed() }
            title = "Registrar"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }


    private fun signUp() {
        binding.signUp.setOnClickListener {
            userNew.apellidos = edtLastname?.text.toString().trim()
            userNew.email = edtEmail?.text.toString().trim()
            userNew.nombres = edtFullname?.text.toString().trim()
            userNew.alias = edtAlias?.text.toString().trim()
            _viewModel.createUser(
                userNew.email?:"",
                edtPass!!.text.toString().trim()
            )
        }
    }

    override fun onDestroy() {
        ViewModelRegistroUsuario.destroyInstance()
        super.onDestroy()
    }
}