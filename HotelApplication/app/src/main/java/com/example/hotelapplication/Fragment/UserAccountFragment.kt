package com.example.hotelapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.UseraccountmainBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Token.AuthClient
import com.example.hotelapplication.viewmodel.ClientViewModel
import com.example.hotelapplication.viewmodel.ClientViewModelFactory
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel
import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory


class UserAccountFragment :  Fragment()  {

    private lateinit var ViewModel: ClientViewModel
    private lateinit var binding: UseraccountmainBinding
    private lateinit var viewModelRegister: RegisterActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("UserAccountFragment", "onCreateView()")
        binding = UseraccountmainBinding.inflate(layoutInflater)
        ViewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)
        viewModelRegister = ViewModelProvider(
            this, RegisterActivityViewModelFactory(
                requireActivity().application,
                ClientRepository(APIService.getService())
            )
        ).get(RegisterActivityViewModel::class.java)
        viewModelRegister._clientIdLiveData.observe(viewLifecycleOwner) { clientId ->
            Log.d("useraccount ", "Client ID: $clientId")
        }
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        //(requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
       // toolbar?.setNavigationIcon(R.drawable.ic_baseline_menu)
        val drawerLayout = view?.findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = binding.navView

        toolbar?.setNavigationOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }


        viewModelRegister.clientName.observe(viewLifecycleOwner) { clientName ->
            val headerView = navigationView.getHeaderView(0)
            val nameTextView = headerView.findViewById<TextView>(R.id.name)
            nameTextView.text = clientName
        }
        val clientName= viewModelRegister.clientName.value
        Log.d("UserAccountFragment ", "clientIdLiveData: ${viewModelRegister.clientName.value}")
        // Set the name and email in the navigation header
        val headerView = binding.navView.getHeaderView(0) // Get the headerView of the NavigationView
        val nameTextView = headerView.findViewById<TextView>(R.id.name)
        val emailTextView = headerView.findViewById<TextView>(R.id.email)
        nameTextView.text = clientName
        //  emailTextView.text = email

        val mainfragment = ProfileFragment()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mainfragment)
                        .addToBackStack(null)
                        .commit()
                    navigationView.setCheckedItem(R.id.nav_home);
                    true
                }

              /*R.id.nav_profile -> {
                    val fragment = EditProfile()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                    navigationView.setCheckedItem(R.id.nav_profile );
                    true
                }*/
                R.id.nav_settings -> {
                    val fragment = ChangePasswordFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                    navigationView.setCheckedItem(R.id.nav_settings );
                    true
                }


                R.id.nav_history-> {
                    val fragment = HistoryFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                    navigationView.setCheckedItem(R.id.nav_settings );
                    true
                }


                /*  R.id.nav_claim -> {
                      val fragment = ClaimFragment()
                      requireActivity().supportFragmentManager.beginTransaction()
                          .replace(R.id.fragment_container, fragment)
                          .addToBackStack(null)
                          .commit()
                      navigationView.setCheckedItem(R.id.nav_claim );
                      true
                  }*/

                R.id.nav_logout -> {
                    AuthClient.getInstance().logout()
                    val fragment = LoginFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .addToBackStack(null)
                        .commit()
                    navigationView.setCheckedItem(R.id.nav_logout);
                    true
                }




                else -> false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("UserAccountFragment", "onViewCreated()")
        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        Log.d("UserAccountFragment", "Name: $name, Email: $email")
        val headerView = binding.navView.getHeaderView(0)

        // Find views within the headerView to update name and email
        val nameTextView = headerView.findViewById<TextView>(R.id.name)
        val emailTextView = headerView.findViewById<TextView>(R.id.email)

        // Observe the LiveData for client name and email
        viewModelRegister.clientName.observe(viewLifecycleOwner) { clientName ->
            // Mettez à jour le TextView correspondant dans le header de la NavigationView
            val headerView = binding.navView.getHeaderView(0)
            val nameTextView = headerView.findViewById<TextView>(R.id.name)
            nameTextView.text = clientName
        }

        viewModelRegister.clientEmail.observe(viewLifecycleOwner) { clientEmail ->
            // Mettez à jour le TextView correspondant dans le header de la NavigationView
            val headerView = binding.navView.getHeaderView(0)
            val emailTextView = headerView.findViewById<TextView>(R.id.email)
            emailTextView.text = clientEmail
        }

        // Appelez la méthode pour récupérer les données du client
        viewModelRegister.fetchClientNameAndEmail()

        // Fetch client name and email using ViewModel
        viewModelRegister.fetchClientNameAndEmail()

        val mainFragment = ProfileFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mainFragment)
            .commit()
    }

    companion object {
        fun newInstance(name: String, email: String): UserAccountFragment {
            val args = Bundle()
            args.putString("name", name)
            args.putString("email", email)
            val fragment = UserAccountFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "User account" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}
