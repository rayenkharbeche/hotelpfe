package com.example.hotelapplication.Fragment

import CommandeViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.PayementBinding
import com.example.hotelapplication.Api.APIService
import com.example.hotelapplication.Repository.ClientRepository
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.Token.AuthClient
import com.example.hotelapplication.model.Command
import com.example.hotelapplication.model.Item
import com.example.hotelapplication.viewmodel.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PayementFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var commandeViewModel: CommandeViewModel
    private lateinit var clientViewModel: ClientViewModel
    private lateinit var pdfFilePath: String
    private lateinit var binding: PayementBinding

    private lateinit var viewModelRegister: RegisterActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PayementBinding.inflate(layoutInflater)
        val payHereRadioButton = binding.radioButton
        val payAtCheckoutRadioButton = binding.radioButton2
        commandeViewModel = ViewModelProvider(
            this,
            CommandeViewModelFactory(
                CommandeRepository(APIService.getService()),
                requireActivity().application
            )
        ).get(CommandeViewModel::class.java)
        clientViewModel = ViewModelProvider(
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
        binding.buttonconfirmPay.setOnClickListener {
            if (payHereRadioButton.isChecked) {
                Log.d("PayementFragment", "Pay here button clicked")
                generatePdf()
                val id = UUID.randomUUID().toString()
                val treatementDuration = arguments?.getString("treatementDuration") ?: ""
                val totalPrice = sharedViewModel.getTotalPrice()
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val serviceList = sharedViewModel.myItemList.value ?: emptyList()
                Log.d("PayementFragment", "serviceList: $serviceList")
                val clientId = viewModelRegister.getClientId().value
                //val commandeList = serviceList.map { it.commands }.distinct().mapNotNull { it }
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
            } else if (payAtCheckoutRadioButton.isChecked) {

                var Credit = sharedViewModel.getTotalPrice()
                AuthClient.getInstance().logout()
                val name = arguments?.getString("name") ?: ""
                val id = arguments?.getString("id") ?: ""
                val totalPrice = arguments?.getFloat("totalPrice") ?: 0f
                openLoginFragment(id, name, Credit,totalPrice)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select a payment option",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    companion object {
        fun newInstance(id: String?, name: String?,totalPrice: Float?): PayementFragment {
            val args = Bundle()
            args.putString("id", id)
            args.putString("name", name)
            args.putFloat("totalPrice", totalPrice ?: 0f)
            val fragment = PayementFragment()
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
            val totalPrice = sharedViewModel.getTotalPrice().toString()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val serviceList = sharedViewModel.myCommandeServiceList.value ?: emptyList()
            PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))

            Log.d("PDF", "Opening document")
            document.open()

            val fontBold = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD)
            val paragraph = Paragraph("Facture du cient ", fontBold)
            paragraph.alignment = Element.ALIGN_CENTER
            document.add(paragraph)
            document.add(Paragraph(date))
            document.add(Paragraph(num))

            val servicesParagraph = Paragraph("Votre commane est :")
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

    private fun openLoginFragment(id: String?, name: String?, credit: Double,totalPrice: Float?) {
        sharedViewModel.hasClickedButton = true
        val secondFragment = LoginFragment.newInstance(id, name, credit,totalPrice)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flFragment, secondFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onResume() {
        super.onResume()
        val fragmentTitle = "Payement" // Replace this with the title of the fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = fragmentTitle
    }
}


