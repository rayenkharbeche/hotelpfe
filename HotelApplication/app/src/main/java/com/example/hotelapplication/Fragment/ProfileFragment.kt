
package com.example.hotelapplication.Fragment
import CommandeViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Adapter.ImageLo
import com.example.hotelapplication.databinding.ContentprofileBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.data.TokenResponse
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.model.Item
import com.example.hotelapplication.viewmodel.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var viewModelRegister: RegisterActivityViewModel
    private lateinit var viewModel: ClientViewModel
    lateinit var commandeViewModel: CommandeViewModel
    private lateinit var binding: ContentprofileBinding

     var clientId: String? = null
    var clientName: String? = null
    private lateinit var pdfFilePath: String
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContentprofileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelRegister = ViewModelProvider(this, RegisterActivityViewModelFactory(
            requireActivity().application,
            ClientRepository(APIService.getService())
        )).get(RegisterActivityViewModel::class.java)
        commandeViewModel = ViewModelProvider(
            this,
            CommandeViewModelFactory(
                CommandeRepository(APIService.getService()),
                requireActivity().application
            )
        )[CommandeViewModel::class.java]
        viewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        )[ClientViewModel::class.java]
        binding.viewModel = viewModelRegister

        // Récupérez les valeurs d'email et d'emailToken depuis les arguments du fragment
        val email = arguments?.getString("email", "")
        val emailToken = arguments?.getString("emailToken", "")
        Log.d("ProfileFragment", "Email: $email, Email Token: $emailToken")

// Vérifiez si les valeurs d'email et d'emailToken ne sont pas nulles ou vides
        val authTokenLiveData: LiveData<String?> = email?.let { email ->
            if (emailToken != null) {
                viewModelRegister.signIn(email, emailToken)
            } else {
                MutableLiveData<String?>().apply { value = null }
            }
        } ?: MutableLiveData<String?>().apply { value = null }

        authTokenLiveData.observe(viewLifecycleOwner) { authToken: String? ->
            if (authToken != null) {
                viewModelRegister.validateAPIToken(authToken)
                    .observe(viewLifecycleOwner) { tokenResponse: TokenResponse? ->
                        if (tokenResponse != null) {
                            // Mettez à jour les MutableLiveData avec les données du tokenResponse
                            viewModelRegister.clientName.value = tokenResponse.client.name
                            Log.d("ProfileFragment", "name: ${tokenResponse.client.name}")
                            viewModelRegister.clientEmail.value = tokenResponse.client.email
                            viewModelRegister.clientPhone.value = tokenResponse.client.phone
                            viewModelRegister.clientCredit.value = tokenResponse.client.credit+" DT"
                            viewModelRegister.clientPhoto.value = tokenResponse?.client?.photo

                            ImageLo.loadImage(binding.profileImage, viewModelRegister.clientPhoto.value)
                            clientId = tokenResponse.client.id
                            clientName = tokenResponse.client.name
                            Log.d("ProfileFragment", "photo: ${ tokenResponse?.client?.photo}")


                        } else {
                            Log.d("ProfileFragment", "TokenResponse is null")

                        }
                    }
            } else {
                Log.d("ProfileFragment", "authToken is null")
            }
        }

        binding.but.setOnClickListener()
        {
            val fragment = SliderFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
            true

        }
        binding.PDF.setOnClickListener(){
            generatePdf()
            val id = UUID.randomUUID().toString()
            val treDuration = arguments?.getString("treDuration") ?: ""
            val totalPrice = sharedViewModel.getTotalPrice()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val serviceList = sharedViewModel.myItemList.value ?: emptyList()
            Log.d("PayementFragment", "serviceList: $serviceList")
            val clientId = viewModelRegister.getClientId().value
            //val commandList = serviceList.map { it.commands }.distinct().mapNotNull { it }
            val facture = Command(
                id = id,
                treatementDuration = treDuration,
                totalPrice = totalPrice.toFloat(),
                date = date,
                clientId =clientId,
                items = serviceList,
            )
            Log.d("PayementFragment", "Creating command: $facture")
            commandeViewModel.createCommand(facture)
            sharedViewModel.clearServices()
            val secondFragment = PaymentHereFragment.newInstance(pdfFilePath)
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .remove(this@ProfileFragment)
                    .commit()
            }
        })


    }

    companion object {
        fun newInstance(clientId: String): EditProfile {
            val args = Bundle()
            args.putString("clientId", clientId)
            val fragment = EditProfile()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(name: String, email: String,photo: String): UserAccountFragment {
            val args = Bundle()
            args.putString("name", name)
            args.putString("email", email)
            args.putString("photo", photo)
            val fragment = UserAccountFragment()
            fragment.arguments = args
            return fragment
        }



    }
    private fun generatePdf() {
        try {
            val document = Document()
            pdfFilePath =
                requireContext().getExternalFilesDir(null)?.absolutePath + File.separator + "facture.pdf"
            //val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))
            val num = arguments?.getString("num") ?: ""
           // val type = arguments?.getString("type") ?: ""
            val totalPrice = sharedViewModel.getTotalPrice().toString()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val serviceList = sharedViewModel.myItemList.value ?: emptyList()
            PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))

            Log.d("PDF", "Opening document")
            document.open()

            val fontBold = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD)
            val paragraph = Paragraph("Facture du cient ", fontBold)
            paragraph.alignment = Element.ALIGN_CENTER
            document.add(paragraph)
            document.add(Paragraph(date))
            document.add(Paragraph(num))

            val servicesParagraph = Paragraph("Votre commande est :")
            for (commandService in serviceList) {
                val commands = commandService.commands ?: emptyList<Command>()
                for (command in commands) {
                    val items = command.items ?: emptyList<Item>()
                    for (item in items) {
                        val itemName = item.name
                        val itemPrice = "${item.price} DT"
                        servicesParagraph.add(Chunk("\n $itemName : $itemPrice\n"))
                    }
                }
            }

            Log.d("PDF", "Adding services paragraph")
            document.add(servicesParagraph)

            val totalPriceParagraph = Paragraph()
            totalPriceParagraph.add(Chunk("Total: "))
            totalPriceParagraph.add(Chunk(totalPrice))
            totalPriceParagraph.add(Chunk(" DT"))

            Log.d("PDF", "Adding total price paragraph")
            document.add(totalPriceParagraph)

            Log.d("PDF", "Closing document")
            document.close()

            this.pdfFilePath = pdfFilePath
            Log.d("PDF", "PDF generation completed. File path: $pdfFilePath")
        } catch (e: Exception) {
            Log.e("PDF", "Error generating PDF: ${e.message}")
            e.printStackTrace()
        }
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "My Profile" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}
