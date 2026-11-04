package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val grupo = view.findViewById<ChipGroup>(R.id.chipGroupTipos)
        val etNuevo = view.findViewById<TextInputEditText>(R.id.etNuevoTipo)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarTipo)

        listOf("Reunión", "Entrega de proyecto", "Práctica", "Examen", "Otro")
            .forEach { agregarChip(grupo, it) }

        btnAgregar.setOnClickListener {
            val nombre = etNuevo.text.toString().trim()
            if (nombre.isEmpty()) {
                etNuevo.error = "Escribe un nombre"
                return@setOnClickListener
            }
            agregarChip(grupo, nombre)
            etNuevo.text?.clear()
        }
    }

    private fun agregarChip(grupo: ChipGroup, texto: String) {
        val chip = Chip(requireContext())
        chip.text = texto
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener { grupo.removeView(chip) }
        grupo.addView(chip)
    }
}
