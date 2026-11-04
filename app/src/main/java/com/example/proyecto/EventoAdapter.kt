package com.example.proyecto

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class EventoAdapter(
    private var items: List<Evento>,
    private val onClick: ((Evento) -> Unit)? = null,
    private val onUbicacion: ((Evento) -> Unit)? = null
) : RecyclerView.Adapter<EventoAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvCategoria: TextView = v.findViewById(R.id.tvCategoria)
        val chipEstatus: Chip = v.findViewById(R.id.chipEstatus)
        val tvFechaHora: TextView = v.findViewById(R.id.tvFechaHora)
        val tvContacto: TextView = v.findViewById(R.id.tvContacto)
        val tvUbicacion: TextView = v.findViewById(R.id.tvUbicacion)
        val btnUbicacion: MaterialButton = v.findViewById(R.id.btnUbicacion)
        val tvDescripcion: TextView = v.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = items[position]
        val ctx = holder.itemView.context

        holder.tvCategoria.text = "Categoría: ${e.categoria}"
        holder.tvFechaHora.text = "Fecha: ${e.fecha} - ${e.hora}"
        holder.tvContacto.text = "Contacto: ${e.contacto}"
        holder.tvUbicacion.text = "Ubicación: ${e.ubicacion}"
        holder.tvDescripcion.text = "Descripción: ${e.descripcion}"

        holder.chipEstatus.text = e.status
        val (colorRes, colorTexto) = when (e.status) {
            "Realizado" -> Pair(R.color.status_realizado, Color.WHITE)
            "Aplazado" -> Pair(R.color.status_aplazado, Color.WHITE)
            else -> Pair(R.color.status_pendiente, Color.BLACK)
        }
        holder.chipEstatus.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(ctx, colorRes)))
        holder.chipEstatus.setTextColor(colorTexto)

        if (onUbicacion != null) {
            holder.btnUbicacion.visibility = View.VISIBLE
            holder.btnUbicacion.setOnClickListener { onUbicacion.invoke(e) }
        } else {
            holder.btnUbicacion.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onClick?.invoke(e) }
    }

    fun actualizar(nuevos: List<Evento>) {
        items = nuevos
        notifyDataSetChanged()
    }
}
