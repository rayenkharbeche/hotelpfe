package com.example.hotelapplication.Fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.data.TokenResponse
import com.example.hotelapplication.databinding.EditProfileBinding
import com.example.hotelapplication.data.UpdateClientBody
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel
import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory

class EditProfile : Fragment(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: EditProfileBinding
    private lateinit var viewModel: ClientViewModel
    private lateinit var viewModelRegister: RegisterActivityViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileBinding.inflate(inflater)
        binding.PhoneNumber.onFocusChangeListener = this
        binding.FullName.onFocusChangeListener = this
        binding.Email.onFocusChangeListener = this

        viewModelRegister = ViewModelProvider(
            this, RegisterActivityViewModelFactory(
                requireActivity().application,
                ClientRepository(APIService.getService())
            )
        ).get(RegisterActivityViewModel::class.java)

        viewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)


        binding.FullName.setOnKeyListener(this)
        binding.PhoneNumber.setOnKeyListener(this)
        binding.edtEmail.setOnKeyListener(this)
        binding.button1.setOnClickListener(this)


        viewModelRegister._clientIdLiveData.observe(viewLifecycleOwner) { clientId ->
            Log.d("EditProfile   viewModelRegister", "Client ID: $clientId")
        }

        return binding.root
    }

    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = binding.Email.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email address is invalid"
        }
        if (errorMessage != null && shouldUpdateView) {
            binding.edtEmail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePhoneNumber(): Boolean {
        var errorMessage: String? = null
        val value = binding.PhoneNumber.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Phone number is required"
        }
        if (errorMessage != null) {
            binding.mobile.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val value = binding.FullName.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Full name is required"
        }
        if (errorMessage != null) {
            binding.edtFullName.apply {
                isErrorEnabled = true
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if ((view != null) && (view.id == R.id.button1)) {
            Log.d("EditProfile", "Button clicked")
            onSubmit()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                binding.FullName.id -> {
                    if (hasFocus) {
                        if (binding.edtFullName.isErrorEnabled) {
                            binding.edtFullName.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }
                }

                binding.Email.id -> {
                    if (hasFocus) {
                        if (binding.edtEmail.isErrorEnabled) {
                            binding.edtEmail.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                binding.PhoneNumber.id -> {
                    if (hasFocus) {
                        if (binding.mobile.isErrorEnabled) {
                            binding.mobile.isErrorEnabled = false
                        }
                    } else {
                        validatePhoneNumber()
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent?.action == KeyEvent.ACTION_UP) {
            onSubmit()
            return true
        }
        return false
    }

    private fun validate(): Boolean {
        return validateFullName() && validateEmail() && validatePhoneNumber()
    }

    private fun onSubmit() {
        Log.d("EditProfile", "Inside onSubmit()")
        if (validate()) {
            val clientId = viewModelRegister.getClientId().value
            Log.d("EditProfil", "clientIdLiveData: ${viewModelRegister.clientIdLiveData.value}")
            if (!clientId.isNullOrEmpty()) {
                Log.d("EditProfil", "Client ID: $clientId")
                val name = binding.FullName.text.toString()
                val email = binding.Email.text.toString()
                val phone = binding.PhoneNumber.text.toString()
                try {
                    val updateClientBody = UpdateClientBody(email, phone, name)
                    viewModel.updateClient(clientId!!, updateClientBody)

                    val fragment = ProfilApresEditFragment.newInstance(name, email, phone)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .commitAllowingStateLoss()

                    viewModel.updatedClient.observe(viewLifecycleOwner) { updatedClient ->
                        Log.d("EditProfile", "Updated client: $updatedClient")
                    }
                } catch (e: Exception) {
                    Log.e("Exception", "Error updating client", e)
                    Log.d("Edit profil fragment", "Error updating client.")
                }
            } else {
                // Gérer le cas où l'ID du client n'est pas disponible
            }
        }
        Log.d("EditProfile", "Outside onSubmit()")
    }
    companion object {
        fun newInstance(clientId: String): ProfilApresEditFragment {
            val args = Bundle()
            args.putString("clientId", clientId)
            val fragment = ProfilApresEditFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Edit Profile" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }

}
