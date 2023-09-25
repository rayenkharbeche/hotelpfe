package com.example.hotelapplication.Fragment


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.R
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.databinding.ContentprofileBinding
import com.example.hotelapplication.databinding.LoginBinding
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel

import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory

class RegistreFragment : Fragment(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var binding: LoginBinding
    private lateinit var viewModel: RegisterActivityViewModel
    private lateinit var bindingc: ContentprofileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginBinding.inflate(layoutInflater)
        binding.etemail.onFocusChangeListener = this
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Si le texte collé contient des nouvelles lignes, le remplacer par une chaîne vide
                if (s?.contains("\n") == true) {
                    binding.etPassword.setText(s.toString().replace("\n", ""))
                }
            }
        })
        binding.bntLoginnn.setOnClickListener(this)

        val tvHavenAccount = binding.tvHavenAccount
        val spannableString = SpannableString(resources.getString(R.string.register_haven_account))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val fragment = LoginFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ds.linkColor
            }
        }

        val loginTextStart = resources.getString(R.string.register_haven_account).indexOf("Login")
        val loginTextEnd = loginTextStart + "Login".length
        spannableString.setSpan(clickableSpan, loginTextStart, loginTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvHavenAccount.text = spannableString
        tvHavenAccount.movementMethod = LinkMovementMethod.getInstance()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .remove(this@RegistreFragment)
                    .commit()
            }
        })

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getIsLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.getIsUniqueEmail().observe(viewLifecycleOwner) { isUniqueEmail ->
            if (validateEmail(shoudUpdateView = false)) {
                binding.tilemail.apply {
                    if (isErrorEnabled) isErrorEnabled = false
                    setStartIconDrawable(R.drawable.check_circle_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                }
            } else {
                val email = binding.etemail.text.toString()
                binding.tilemail.apply {

                    isErrorEnabled = true
                  //  error = "Email is already taken"
                }

            }
        }


        viewModel.getErrorMessage().observe(viewLifecycleOwner, Observer { errorMessage ->
            val fromErrorKeys = arrayOf("email", "password")
            val message = StringBuilder()
            fromErrorKeys.forEach { errorKey ->
                if (errorMessage.startsWith("$errorKey:")) {
                    val value = errorMessage.removePrefix("$errorKey:")
                    when (errorKey) {
                        "email" -> {
                            binding.tilemail.apply {
                                isErrorEnabled = true
                                error = value
                            }
                        }
                        "password" -> {
                            binding.tilpassword.apply {
                                isErrorEnabled = true
                                error = value
                            }
                        }
                    }
                } else {
                    message.append(errorMessage).append("\n")
                }
            }

            if (message.isNotEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.baseline_info_24)
                    .setTitle("Information")
                    .setMessage(message)
                    .setPositiveButton("ok") { dialog, _ -> dialog!!.dismiss() }
                    .show()
            }
        })


    }

    private fun validateEmail(shoudUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = binding.etemail.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email address is invalid"
        }
        if (errorMessage != null && shoudUpdateView) {
            binding.tilemail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = binding.etPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }
        if (errorMessage != null) {
            binding.tilpassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (binding.root != null && view != null) {
            when (view.id) {
                binding.etemail.id -> {
                    if (hasFocus) {
                        if (binding.tilemail.isErrorEnabled) {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bnt_loginnn -> onSubmit()
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent?.action == KeyEvent.ACTION_UP) {
            onSubmit()
        }
        return false
    }
    private fun onSubmit() {
        if (validate()) {
            val email = binding.etemail.text.toString()
            val emailToken = binding.etPassword.text.toString()

            Log.d("Submit", "Email: $email, emailToken: $emailToken")

            viewModel.signIn(email, emailToken)
            val bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("emailToken", emailToken)
            val fragment = ProfileFragment()
            fragment.arguments = bundle  // Passez les arguments au fragment


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commitAllowingStateLoss()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = RegisterActivityViewModelFactory(requireActivity().application, ClientRepository(APIService.getService()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterActivityViewModel::class.java)
        setupObservers()
    }

    private fun validate(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        return emailValid && passwordValid
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Login" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}
