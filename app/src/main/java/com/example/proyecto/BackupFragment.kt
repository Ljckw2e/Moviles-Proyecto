package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class BackupFragment : Fragment(R.layout.fragment_backup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restaurar = arguments?.getBoolean("restaurar") ?: false
        val accion = if (restaurar) "Restaurar" else "Respaldar"

        view.findViewById<TextView>(R.id.tvTituloBackup).text =
            if (restaurar) "Restaurar datos desde la nube" else "Respaldar eventos en la nube"
        view.findViewById<Button>(R.id.btnDropbox).text = "$accion con Dropbox"
        view.findViewById<Button>(R.id.btnDrive).text = "$accion con Google Drive"

        listOf(R.id.btnDropbox, R.id.btnDrive).forEach { id ->
            view.findViewById<Button>(id).setOnClickListener {
                Toast.makeText(requireContext(), "Disponible en la siguiente entrega", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
