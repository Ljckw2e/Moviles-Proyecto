package com.example.proyecto

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConsultFragment : Fragment(R.layout.fragment_consult) {

    private enum class Modo { DIA, RANGO, MES, ANIO }

    private val todas = "Todas las categorías"
    private var modo = Modo.DIA
    private var categoria = todas
    private var fechaIni: Calendar? = null
    private var fechaFin: Calendar? = null

    private lateinit var adapter: EventoAdapter
    private lateinit var btnFecha: MaterialButton
    private lateinit var tvVacio: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvConsulta)
        val actvCategoria = view.findViewById<MaterialAutoCompleteTextView>(R.id.actvCategoria)
        val toggle = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleModo)
        val btnLimpiar = view.findViewById<MaterialButton>(R.id.btnLimpiar)
        btnFecha = view.findViewById(R.id.btnFecha)
        tvVacio = view.findViewById(R.id.tvVacio)

        adapter = EventoAdapter(
            emptyList(),
            onClick = { mostrarDialogoEstatus(it) },
            onUbicacion = {
                Toast.makeText(requireContext(), "Mapa de: ${it.ubicacion}", Toast.LENGTH_SHORT).show()
            }
        )
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // Filtro por categoría
        val categorias = listOf(todas, "Reunión", "Entrega de proyecto", "Práctica", "Examen", "Otro")
        actvCategoria.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categorias))
        actvCategoria.setText(todas, false)
        actvCategoria.setOnItemClickListener { _, _, pos, _ ->
            categoria = categorias[pos]
            aplicarFiltros()
        }

        // Filtro por Día / Rango / Mes / Año
        toggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                modo = when (checkedId) {
                    R.id.btnRango -> Modo.RANGO
                    R.id.btnMes -> Modo.MES
                    R.id.btnAnio -> Modo.ANIO
                    else -> Modo.DIA
                }
                fechaIni = null
                fechaFin = null
                aplicarFiltros()
            }
        }
        toggle.check(R.id.btnDia)

        btnFecha.setOnClickListener {
            if (modo == Modo.RANGO) {
                elegirFecha { ini ->
                    elegirFecha { fin ->
                        if (fin.before(ini)) { fechaIni = fin; fechaFin = ini } else { fechaIni = ini; fechaFin = fin }
                        aplicarFiltros()
                    }
                }
            } else {
                elegirFecha { f ->
                    fechaIni = f
                    fechaFin = null
                    aplicarFiltros()
                }
            }
        }

        btnLimpiar.setOnClickListener {
            categoria = todas
            actvCategoria.setText(todas, false)
            fechaIni = null
            fechaFin = null
            aplicarFiltros()
        }
    }

    private fun elegirFecha(alElegir: (Calendar) -> Unit) {
        val hoy = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            alElegir(Calendar.getInstance().apply { clear(); set(y, m, d) })
        }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun aplicarFiltros() {
        var lista: List<Evento> = EventoRepository.eventos
        if (categoria != todas) lista = lista.filter { it.categoria == categoria }

        val ini = fechaIni
        if (ini != null) {
            lista = lista.filter { ev ->
                val d = ev.fechaDate() ?: return@filter false
                val c = Calendar.getInstance().apply { time = d }
                when (modo) {
                    Modo.DIA -> c.get(Calendar.YEAR) == ini.get(Calendar.YEAR) &&
                            c.get(Calendar.DAY_OF_YEAR) == ini.get(Calendar.DAY_OF_YEAR)
                    Modo.RANGO -> {
                        val fin = fechaFin ?: ini
                        !d.before(ini.time) && !d.after(fin.time)
                    }
                    Modo.MES -> c.get(Calendar.YEAR) == ini.get(Calendar.YEAR) &&
                            c.get(Calendar.MONTH) == ini.get(Calendar.MONTH)
                    Modo.ANIO -> c.get(Calendar.YEAR) == ini.get(Calendar.YEAR)
                }
            }
        }

        val resultado = EventoRepository.ordenados(lista)
        adapter.actualizar(resultado)
        tvVacio.visibility = if (resultado.isEmpty()) View.VISIBLE else View.GONE
        btnFecha.text = textoFecha()
    }

    private fun textoFecha(): String {
        val ini = fechaIni ?: return "Seleccionar fecha"
        return when (modo) {
            Modo.DIA -> Evento.FORMATO.format(ini.time)
            Modo.RANGO -> Evento.FORMATO.format(ini.time) + " – " + Evento.FORMATO.format((fechaFin ?: ini).time)
            Modo.MES -> SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("es-MX")).format(ini.time)
            Modo.ANIO -> ini.get(Calendar.YEAR).toString()
        }
    }

    private fun mostrarDialogoEstatus(ev: Evento) {
        val opciones = arrayOf("Pendiente", "Aplazado", "Realizado")
        var elegido = opciones.indexOf(ev.status).coerceAtLeast(0)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Elige el estatus")
            .setSingleChoiceItems(opciones, elegido) { _, which -> elegido = which }
            .setPositiveButton("Actualizar") { _, _ ->
                ev.status = opciones[elegido]
                aplicarFiltros()
            }
            .setNeutralButton("Eliminar") { _, _ ->
                EventoRepository.eventos.remove(ev)
                aplicarFiltros()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
