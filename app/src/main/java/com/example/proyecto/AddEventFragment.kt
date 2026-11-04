package com.example.proyecto

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Locale

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipsCategoria = view.findViewById<ChipGroup>(R.id.chipGroupCategoria)
        val chipsRecordatorio = view.findViewById<ChipGroup>(R.id.chipGroupRecordatorio)
        val tilDescripcion = view.findViewById<TextInputLayout>(R.id.tilDescripcion)
        val tilFecha = view.findViewById<TextInputLayout>(R.id.tilFecha)
        val tilHora = view.findViewById<TextInputLayout>(R.id.tilHora)
        val tilUbicacion = view.findViewById<TextInputLayout>(R.id.tilUbicacion)
        val etDescripcion = view.findViewById<TextInputEditText>(R.id.etDescripcion)
        val etFecha = view.findViewById<TextInputEditText>(R.id.etFecha)
        val etHora = view.findViewById<TextInputEditText>(R.id.etHora)
        val etUbicacion = view.findViewById<TextInputEditText>(R.id.etUbicacion)
        val actvStatus = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvStatus)
        val actvContacto = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvContacto)
        val btnGuardar = view.findViewById<MaterialButton>(R.id.btnGuardar)

        val estatus = listOf("Pendiente", "Realizado", "Aplazado")
        actvStatus.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, estatus))
        actvStatus.setText(estatus[0], false)


        val contactos = listOf("Alejandro", "María López", "Carlos Ramírez", "Ana Torres")
        actvContacto.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, contactos))
        actvContacto.setText(contactos[0], false)

        etFecha.setOnClickListener { mostrarDatePicker(etFecha) }
        etHora.setOnClickListener { mostrarTimePicker(etHora) }

        tilUbicacion.setEndIconOnClickListener {
            Toast.makeText(requireContext(), "El selector de mapa se agrega en la siguiente entrega", Toast.LENGTH_SHORT).show()
        }

        btnGuardar.setOnClickListener {
            var valido = true

            if (etDescripcion.text.isNullOrBlank()) {
                tilDescripcion.error = "Escribe una descripción"; valido = false
            } else tilDescripcion.error = null

            if (etFecha.text.isNullOrBlank()) {
                tilFecha.error = "Elige la fecha"; valido = false
            } else tilFecha.error = null

            if (etHora.text.isNullOrBlank()) {
                tilHora.error = "Elige la hora"; valido = false
            } else tilHora.error = null

            if (!valido) return@setOnClickListener

            val categoria = view.findViewById<Chip>(chipsCategoria.checkedChipId).text.toString()
            val recordatorio = view.findViewById<Chip>(chipsRecordatorio.checkedChipId).text.toString()

            EventoRepository.eventos.add(
                Evento(
                    EventoRepository.nuevoId(), categoria,
                    etFecha.text.toString(), etHora.text.toString(),
                    etDescripcion.text.toString().trim(),
                    actvStatus.text.toString(),
                    etUbicacion.text.toString().trim(),
                    actvContacto.text.toString(),
                    recordatorio
                )
            )
            Toast.makeText(requireContext(), "Evento guardado", Toast.LENGTH_SHORT).show()

            val opciones = NavOptions.Builder().setPopUpTo(R.id.bottom_home, true).build()
            findNavController().navigate(R.id.bottom_home, null, opciones)
        }
    }

    private fun mostrarDatePicker(editText: TextInputEditText) {
        val c = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            editText.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun mostrarTimePicker(editText: TextInputEditText) {
        val c = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, hour, minute ->
            editText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }
}
