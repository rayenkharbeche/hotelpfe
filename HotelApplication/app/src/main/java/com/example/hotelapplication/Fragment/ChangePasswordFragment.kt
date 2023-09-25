package com.example.hotelapplication.Fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.ChangepasswordBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.google.android.material.snackbar.Snackbar


class ChangePasswordFragment : Fragment(), View.OnClickListener, View.OnFocusChangeListener,
View.OnKeyListener {

    private lateinit var binding: ChangepasswordBinding
    private lateinit var ViewModel: ClientViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChangepasswordBinding.inflate(layoutInflater)
        binding.edtSignUpPassword.onFocusChangeListener = this
        binding.edtSignUpConfirmPassword.onFocusChangeListener = this
        binding.edtSignUpoldPassword.onFocusChangeListener= this
        binding.button1.setOnClickListener(this)
        ViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)


        return binding.root  }




    private fun validatePassword(): Boolean {

        var errorMessage: String? = null
        val value = binding.edtSignUpPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }
        if (errorMessage != null) {
            binding.password.apply {
                isErrorEnabled = true
                error = errorMessage
            }

        }
        return errorMessage == null
    }


    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value = binding.edtSignUpConfirmPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Confirm password is required"
        } else if (value.length < 6) {
            errorMessage = "Confirm password must be 6 characters long"
        }
        if (errorMessage != null) {
            binding.cpassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = binding.edtSignUpPassword.text.toString()
        val confirmPassword = binding.edtSignUpConfirmPassword.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Confirm password doesn't match with password"
        }
        if (errorMessage != null) {
            binding.cpassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }



    override fun onClick(view: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (binding.root != null && view != null) {
            when (view.id) {
        binding.edtSignUpPassword.id -> {
            if (hasFocus) {
                if (binding.password.isErrorEnabled) {
                    binding.password.isErrorEnabled = false
                }
            } else {
                if (validatePassword() && binding.edtSignUpConfirmPassword.text!!.isNotEmpty() && validateConfirmPassword() &&
                    validatePasswordAndConfirmPassword()
                ) {
                    if (binding.cpassword.isErrorEnabled) {
                        binding.cpassword.isErrorEnabled = false
                    }
                    binding.cpassword.apply {
                        setStartIconDrawable(R.drawable.check_circle_24)
                        setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    }
                }
            }
        }


        binding.edtSignUpConfirmPassword.id -> {
            if (hasFocus) {
                if (binding.cpassword.isErrorEnabled) {
                    binding.cpassword.isErrorEnabled = false
                }
            } else {
                if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                    if (binding.password.isErrorEnabled) {
                        binding.password.isErrorEnabled = false
                    }
                    binding.cpassword.apply {
                        setStartIconDrawable(R.drawable.check_circle_24)
                        setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    }
                }
            }
        }


    }
}
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP) {
           // onSubmit()
        }

        return false
    }


    private fun validate(): Boolean {
        var isValid = true
        if (!validatePassword()) isValid = false
        if (!validateConfirmPassword()) isValid = false
        return isValid
    }



 /*   private fun onSubmit() {
        if (validate()) {
            ViewModel.client.value?.let { client ->
                if (binding.edtSignUpoldPassword.text.toString() == client.password) {
                    if (binding.edtSignUpPassword.text.toString() == binding.edtSignUpConfirmPassword.text.toString()) {
                        try {
                            client.password = binding.edtSignUpPassword.text.toString()
                            client.id?.let { ViewModel.updateClient(it, client) }
                            val snackbar = Snackbar.make(binding.root, "Your information has been updated!", Snackbar.LENGTH_SHORT)
                            val view = snackbar.view
                            val params = view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.CENTER
                            view.layoutParams = params
                            view.setBackgroundColor(ContextCompat.getColor(requireContext(), androidx.appcompat.R.color.material_deep_teal_200))
                            snackbar.show()
                        } catch (e: Exception) {
                            Log.e("Exception", "Error updating client", e)
                        }
                    } else {
                        val snackbar = Snackbar.make(binding.root, "Passwords do not match", Snackbar.LENGTH_SHORT)
                        val view = snackbar.view
                        val params = view.layoutParams as FrameLayout.LayoutParams
                        params.gravity = Gravity.CENTER
                        view.layoutParams = params
                        view.setBackgroundColor(ContextCompat.getColor(requireContext(), androidx.appcompat.R.color.error_color_material_dark))
                        snackbar.show()
                    }
                } else {
                    val snackbar = Snackbar.make(binding.root, "Old password is incorrect", Snackbar.LENGTH_SHORT)
                    val view = snackbar.view
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.CENTER
                    view.layoutParams = params
                    view.setBackgroundColor(ContextCompat.getColor(requireContext(), androidx.appcompat.R.color.error_color_material_dark))
                    snackbar.show()
                }
            }*/
        }

