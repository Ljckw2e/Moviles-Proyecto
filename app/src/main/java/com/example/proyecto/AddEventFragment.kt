package com.example.proyecto

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


class AddEventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etFecha = view.findViewById<TextInputEditText>(R.id.etFecha)
        val etHora = view.findViewById<TextInputEditText>(R.id.etHora)

        etFecha.setOnClickListener {
            mostrarDatePicker(etFecha)
        }

        etHora.setOnClickListener {
            mostrarTimePicker(etHora)
        }
    }

    private fun mostrarDatePicker(editText: TextInputEditText) {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            // Se suma 1 al mes porque en Java/Kotlin los meses inician en 0
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            editText.setText(fechaSeleccionada)
        }, anio, mes, dia)

        datePickerDialog.show()
    }

    private fun mostrarTimePicker(editText: TextInputEditText) {
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minuto = calendario.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            val horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute)
            editText.setText(horaSeleccionada)
        }, hora, minuto, true)

        timePickerDialog.show()
    }
}