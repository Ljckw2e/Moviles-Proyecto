package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var adapter: EventoAdapter
    private lateinit var tvVacio: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVacio = view.findViewById(R.id.tvVacio)
        val rv = view.findViewById<RecyclerView>(R.id.rvEventos)
        adapter = EventoAdapter(emptyList())
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val lista = EventoRepository.proximos(4) // hoy + 4 días
        adapter.actualizar(lista)
        tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}
