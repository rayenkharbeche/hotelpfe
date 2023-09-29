package com.example.hotelapplication.Fragment


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Api.VibrateView
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.FragmentEmailVerificationBinding
import com.example.hotelapplication.model.Client
import com.example.hotelapplication.viewmodel.EmailVerificationViewModel


class EmailVerificationFragment : Fragment(), View.OnClickListener, View.OnFocusChangeListener,View.OnKeyListener {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnVerify: Button
    private val viewModel: EmailVerificationViewModel by viewModels {
        ViewModelProvider.NewInstanceFactory()
    }
    private lateinit var binding: FragmentEmailVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailVerificationBinding.inflate(inflater, container, false)
        val view = binding.root

        etEmail = binding.etemail
        etPassword = binding.etPassword
        btnVerify = binding.bntLoginemailpass
        etEmail.setOnClickListener(this)
        etPassword.setOnClickListener(this)
        etEmail.setOnKeyListener(this)
        etPassword.setOnKeyListener(this)

        btnVerify.setOnClickListener(this)

        btnVerify.setOnClickListener {

            if (validateEmail() && validatePassword()) {
            // Effectuer la vérification côté client avec le token envoyé par email
                val url = "http://example.com/client/auth/token?jwt=YOUR_JWT_TOKEN"
                val uri = Uri.parse(url)


               // viewModel.loginWithEmailAndPassword(email, password)
            }
        }

        return view
    }

    private fun startFragment(fragment: Fragment) {
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.flFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun validateEmail(shouldUpdateView:Boolean = true,shouldVibrateView:Boolean = true):Boolean{
        var errorMessage : String?= null
        var value : String =binding.etemail.text.toString()
        if (value.isEmpty()){
            errorMessage="Email is required"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage="Email adress invalid"
        }
        if(errorMessage!=null){
            binding.tilemail.apply {
                isErrorEnabled=true
                error= errorMessage
                if(shouldVibrateView)  VibrateView.vibrate(requireContext(),this)
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(shouldVibrateView:Boolean = true):Boolean{
        var errorMessage : String?= null
        var value : String =binding.etPassword.text.toString()
        if (value.isEmpty()){
            errorMessage="Pasword is required"
        }else if(value.length<4){
            errorMessage="Password must be 4 characters long"
        }

        if(errorMessage!=null){
            binding.tilpassword.apply {
                isErrorEnabled=true
                error= errorMessage
                if(shouldVibrateView) VibrateView.vibrate(requireContext(),this)
            }
        }
        return errorMessage == null
    }
    private fun validate() : Boolean{
        var isvalid = true
        if(!validateEmail(shouldVibrateView= false)) isvalid = false
        if(!validatePassword(shouldVibrateView= false)) isvalid = false

        return isvalid
    }
    private fun onSubmit() {
        if (validate()) {
            val email = binding.etemail.text.toString()
            val password = binding.etPassword.text.toString()
           // viewModel.loginWithEmailAndPassword(email, password)
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bnt_loginemailpass -> {
                onSubmit()
                val fragment = ProfileFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
                Log.d("click login","click login")
            }

        }

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {

                R.id.etemail -> {
                    if (hasFocus) {
                        if (binding.tilemail.isCounterEnabled) {
                            binding.tilemail.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()

                    }
                }
                R.id.etPassword -> {
                    if (hasFocus) {
                        if (binding.tilpassword.isCounterEnabled) {
                            binding.tilpassword.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {

        if (KeyEvent.KEYCODE_ENTER == keyCode && event!!.action == KeyEvent.ACTION_UP) {

            onSubmit()
        }
        return true
    }



}
