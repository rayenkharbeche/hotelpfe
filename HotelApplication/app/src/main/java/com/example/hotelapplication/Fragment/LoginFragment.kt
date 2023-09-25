package com.example.hotelapplication.Fragment

import CommandeViewModel
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.data.LoginBody
import com.example.hotelapplication.databinding.LoginemailBinding
import com.example.hotelapplication.viewmodel.*

class LoginFragment : Fragment(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var binding: LoginemailBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var clientViewModel: ClientViewModel
    private lateinit var commandeViewModel: CommandeViewModel
   // private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginemailBinding.inflate(layoutInflater)
        binding.bntLogin.setOnClickListener(this)
        binding.etemail.onFocusChangeListener = this

        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(ClientRepository(APIService.getService()), requireActivity().application,requireContext())
        ).get(LoginViewModel::class.java)

        clientViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)

        commandeViewModel = ViewModelProvider(
            this,
            CommandeViewModelFactory(
                CommandeRepository(APIService.getService()),
                requireActivity().application
            )
        ).get(CommandeViewModel::class.java)

        setupObservers()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .remove(this@LoginFragment)
                    .commit()
            }
        })

        return binding.root
    }

    private fun setupObservers() {
        viewModel.getIsLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.getErrorMessage().observe(viewLifecycleOwner) { errorMap ->
            val formErrorKeys = arrayOf("email")
            val message = StringBuilder()

            errorMap.map { entry ->
                if (formErrorKeys.contains(entry.key)) {
                    when (entry.key) {
                        "email" -> {
                            binding.tilemail.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                        // Add branches for other form error keys if necessary
                        else -> {
                            // Handle other form error keys here
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }
            }

            if (message.isNotEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.info_24)
                    .setTitle("INFORMATION")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ -> dialog?.dismiss() }
                    .show()
            }
        }

        viewModel.signInResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success) {
                    // Handle successful sign-in
                    // Navigate to the appropriate fragment
                    val fragment = ProfileFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .commit()
                } else {
                    // Handle sign-in failure
                    // Show error message to the user
                    val errorMessage = response.error
                    AlertDialog.Builder(requireContext())
                        .setIcon(R.drawable.info_24)
                        .setTitle(R.string.login_error_title)
                        .setMessage(errorMessage as CharSequence) // Specify the type explicitly
                        .setPositiveButton("OK") { dialog, _ -> dialog?.dismiss() }
                        .show()
                }
            }
        }
    }

    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = binding.etemail.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email address is invalid"
        }
        if (errorMessage != null && shouldUpdateView) {
            binding.tilemail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.bnt_login -> {
                    submitForm()
                }
            }
        }
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
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        if (event == KeyEvent.KEYCODE_ENTER && keyEvent!!.action == KeyEvent.ACTION_UP) {
            submitForm()
        }
        return false
    }

    private fun validate(): Boolean {
        var isValid = true
        if (!validateEmail()) isValid = false
        return isValid
    }

    private fun submitForm() {
        if (validate()) {
            val email = binding.etemail.text.toString()
            val loginBody = LoginBody(email)
            viewModel.signUp(loginBody)
            val fragment =RegistreFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }}


    companion object {
        fun newInstance(id: String?, name: String?, credit: Double,totalPrice: Float?): LoginFragment {
            val args = Bundle()
            args.putString("id", id)
            args.putString("name", name)
            args.putDouble("credit", credit)
            args.putFloat("totalPrice", totalPrice ?: 0f)
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Hotel Wallet" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}
