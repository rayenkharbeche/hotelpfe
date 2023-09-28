package com.example.hotelapplication.Fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hotelapplication.databinding.ThanksBinding
import com.example.hotelapplication.viewmodel.SharedViewModel
import java.io.*

class PaymentHereFragment: Fragment() {

    private lateinit var binding: ThanksBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ThanksBinding.inflate(layoutInflater)
        val pdfFilePath = arguments?.getString("pdfFilePath")
        binding.btnconf.setOnClickListener()

        {
            openPdf(pdfFilePath)
        }



        return binding.root
    }

    companion object {
        fun newInstance(pdfFilePath: String): PaymentHereFragment {
            val args = Bundle()
            args.putString("pdfFilePath", pdfFilePath)
            val fragment = PaymentHereFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun openPdf(pdfFilePath: String?) {
        if (!pdfFilePath.isNullOrEmpty()) {
            val file = File(pdfFilePath)
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(requireContext(), "com.example.hotelapplication.fileprovider", file)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ajoutez cette ligne pour démarrer l'activité dans un nouveau contexte de tâche
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "Aucune application de visualisation de PDF n'a été trouvée", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Fichier PDF introuvable", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Chemin de fichier PDF invalide", Toast.LENGTH_SHORT).show()
        }
    }

}

