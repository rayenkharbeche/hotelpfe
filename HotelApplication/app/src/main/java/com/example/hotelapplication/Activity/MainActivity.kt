package com.example.hotelapplication.Activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.hotelapplication.Adapter.ImageLo
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Fragment.*
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.databinding.ActivityMainBinding
import com.example.hotelapplication.viewmodel.RegisterActivityViewModel
import com.example.hotelapplication.viewmodel.RegisterActivityViewModelFactory
import com.example.hotelapplication.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(
            SharedViewModel::class.java
        )
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var viewModelRegister: RegisterActivityViewModel
    private var searchMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.navView.getHeaderView(0).findViewById(R.id.imageView)
        nameTextView = binding.navView.getHeaderView(0).findViewById(R.id.name)
        emailTextView = binding.navView.getHeaderView(0).findViewById(R.id.email)
      //  Glide.with(applicationContext)


        // Hide the image, name, and email initially
        imageView.visibility = View.INVISIBLE
        nameTextView.visibility = View.INVISIBLE
        emailTextView.visibility = View.INVISIBLE
        viewModelRegister = ViewModelProvider(
            this,
            RegisterActivityViewModelFactory(
                application,
                ClientRepository(APIService.getService())
            )
        ).get(RegisterActivityViewModel::class.java)

        val name = intent.getStringExtra("name")
        val firstFragment = ServiceListFragment.newInstance(name)
        setCurrentFragment(firstFragment)
        val secondFragment = MyCardFragment.newInstance(name)
        val thirdFragment = LoginFragment()
        val profile = EditProfile()
        val profilee = ProfileFragment()
        val history = HistoryFragment()
        val stat = StatByTypeFragment()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> setCurrentFragment(firstFragment)
                R.id.list -> setCurrentFragment(secondFragment)
                R.id.user -> setCurrentFragment(thirdFragment)
                R.id.qrcode -> Qrcode()
            }
            true
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val topLevelDestinationIds = setOf(
            R.id.nav_home,
          //  R.id.nav_profile,
            R.id.nav_settings,
            R.id.nav_history,
            R.id.nav_claim,
            R.id.nav_logout
        )
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds,
            drawerLayout
        )

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> setCurrentFragment(firstFragment)
              //  R.id.nav_profile -> setCurrentFragment(profilee)
                R.id.nav_settings -> setCurrentFragment(profile)
                R.id.nav_history -> setCurrentFragment(history)
                R.id.nav_claim -> setCurrentFragment(stat)
                R.id.nav_logout -> Qrcode()
            }
            true
        }
       // updateClientInfo()
        // Hide the image, name, and email initially
        imageView.visibility = View.GONE
        nameTextView.visibility = View.GONE
        emailTextView.visibility = View.GONE

        // Ajouter un listener pour détecter les changements d'état du clavier
        val rootView =
            findViewById<View>(R.id.flFragment) // Remplacez rootView par l'id de votre racine de vue principale
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val isKeyboardOpen = isKeyboardOpen()
            setMenuItemVisibility(!isKeyboardOpen)
        }
    }

    // Méthode pour vérifier si le clavier est ouvert ou fermé
    private fun isKeyboardOpen(): Boolean {
        val rootView =
            findViewById<View>(R.id.flFragment) // Remplacez rootView par l'id de votre racine de vue principale
        val screenHeight = rootView.height
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val keypadHeight = screenHeight - rect.bottom
        return keypadHeight > screenHeight * 0.15 // Vous pouvez ajuster cette valeur selon vos besoins
    }

    fun setMenuItemVisibility(isVisible: Boolean) {
        searchMenuItem?.isVisible = isVisible
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        searchMenuItem = menu.findItem(R.id.search)
        updateClientInfo()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.flFragment)
        updateClientInfo()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()

        }

    private fun Qrcode() {
        val intent = Intent(this, QrCodeActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
            Log.d("MainActivity", "onBackPressed()")
            updateClientInfo()
            binding.drawerLayout.closeDrawer(binding.navView)
        } else {
            super.onBackPressed()
        }
    }

     fun updateClientInfo() {
         Log.d("MainActivity", "updateClientInfo()")

         val clientId = viewModelRegister.getClientNamee().value
         Log.d("MainActivity", " clientName:$clientId")
         nameTextView.text =  clientId


         val clientemail = viewModelRegister.getClientemail().value
         Log.d("MainActivity", " clientemail:$clientemail")
         emailTextView.text = clientemail


         val clientPhoto = viewModelRegister.getClientPhotoo().value
         Log.d("MainActivity", " clientPhoto:$clientPhoto")
         ImageLo.loadImage(imageView, clientPhoto)
        /*
         Glide.with(this)

             .load(clientPhoto)
             .into(imageView) */

             // Afficher les vues du navheader
             imageView.visibility = View.VISIBLE
             nameTextView.visibility = View.VISIBLE
             emailTextView.visibility = View.VISIBLE


     }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Masquer le MenuItem de recherche lorsque le SearchView est actif
        val searchView = searchMenuItem?.actionView as? SearchView
        val isSearchViewExpanded = searchView?.isIconified == false
        val isKeyboardOpen = isKeyboardOpen()
        setMenuItemVisibility(!isSearchViewExpanded && !isKeyboardOpen)

        return super.onPrepareOptionsMenu(menu)
    }
}