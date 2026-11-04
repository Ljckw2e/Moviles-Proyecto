package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private lateinit var adapter: EventoAdapter
    private lateinit var tvVacio: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val rv = view.findViewById<RecyclerView>(R.id.rvEventosDia)
        tvVacio = view.findViewById(R.id.tvVacio)

        adapter = EventoAdapter(emptyList())
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        mostrarDia(Evento.FORMATO.format(Date(calendarView.date)))
        calendarView.setOnDateChangeListener { _, y, m, d ->
            mostrarDia(String.format(Locale.getDefault(), "%02d/%02d/%04d", d, m + 1, y))
        }
    }

    private fun mostrarDia(fecha: String) {
        val lista = EventoRepository.ordenados(EventoRepository.eventos.filter { it.fecha == fecha })
        adapter.actualizar(lista)
        tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}
