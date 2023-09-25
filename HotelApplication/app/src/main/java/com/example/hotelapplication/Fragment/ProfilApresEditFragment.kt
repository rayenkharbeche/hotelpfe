package com.example.hotelapplication.Fragment

import CommandeViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.R
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.databinding.FragmentProfilApresEditBinding
import com.example.hotelapplication.model.Client
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.model.Item
import com.example.hotelapplication.viewmodel.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfilApresEditFragment : Fragment() {

    private lateinit var binding: FragmentProfilApresEditBinding
    private lateinit var viewModel: ClientViewModel
    private lateinit var viewModelRegister: RegisterActivityViewModel
    lateinit var commandeViewModel: CommandeViewModel
    private lateinit var pdfFilePath: String
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilApresEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ClientViewModelFactory(
                ClientRepository(APIService.getUpdatedService()),
                requireActivity().application
            )
        ).get(ClientViewModel::class.java)

        viewModelRegister = ViewModelProvider(this, RegisterActivityViewModelFactory(
            requireActivity().application,
            ClientRepository(APIService.getService())
        )
        ).get(RegisterActivityViewModel::class.java)
        commandeViewModel = ViewModelProvider(
            this,
            CommandeViewModelFactory(
                CommandeRepository(APIService.getService()),
                requireActivity().application
            )
        ).get(CommandeViewModel::class.java)
        // Observer les changements du client mis à jour



        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        val phone = arguments?.getString("phone")
        Log.d("profil apres Edit", "Name: $name, Email: $email, Phone: $phone")
                binding.tv1.text = name.toString()
                binding.tv2.text = email
                binding.tv3.text = phone

                Log.d("profil apres Edit ", "$name")
        // Définir la liaison des données
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
            val treatementDuration = arguments?.getString("treatementDuration") ?: ""
            val totalPrice = sharedViewModel.getTotalPrice()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val serviceList = sharedViewModel.myItemList.value ?: emptyList()
            Log.d("PayementFragment", "serviceList: $serviceList")
            val clientId = viewModelRegister.getClientId().value
            val commandeList = serviceList.map { it.commands }.distinct().mapNotNull { it }
            val facture = Command(
                id = id,
                treatementDuration = treatementDuration,
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
                    .remove(this@ProfilApresEditFragment)
                    .commit()
            }
        })
        binding.lifecycleOwner = viewLifecycleOwner
    //    binding.viewModel = viewModel
    }

    companion object {
        fun newInstance(name: String, email: String, phone: String):ProfilApresEditFragment  {
            val args = Bundle()
            args.putString("name", name)
            args.putString("email", email)
            args.putString("phone", phone)
            val fragment = ProfilApresEditFragment ()
            fragment.arguments = args
            return fragment
        }
        fun newInstancee(client: Client):StatByTypeFragment {
            val args = Bundle()
            args.putString("client", client.toString())

            val fragment = StatByTypeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private fun generatePdf() {
        try {
            val document = Document()
            pdfFilePath =
                requireContext().getExternalFilesDir(null)?.absolutePath + File.separator + "facture.pdf"
            val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))
            val num = arguments?.getString("num") ?: ""
            val type = arguments?.getString("type") ?: ""
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
            for (commandeService in serviceList) {
                val commands = commandeService.commands ?: emptyList<Command>()
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
        val fragmentTitle = "Profile after modification" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}