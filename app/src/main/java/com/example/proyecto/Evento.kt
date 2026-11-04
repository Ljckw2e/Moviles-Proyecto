package com.example.proyecto

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Evento(
    val id: Int,
    var categoria: String,
    var fecha: String,
    var hora: String,
    var descripcion: String,
    var status: String,
    var ubicacion: String,
    var contacto: String,
    var recordatorio: String
) {
    fun fechaDate(): Date? = try {
        FORMATO.parse(fecha)
    } catch (e: Exception) {
        null
    }

    companion object {
        val FORMATO = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
}
object EventoRepository {
    private var siguienteId = 1
    val eventos = mutableListOf<Evento>()

    init {
        demo("Reunión", 0, "10:00", "Revisión del primer avance de la app", "Pendiente", "Auditorio Principal", "Alejandro", "10 min antes")
        demo("Examen", 1, "08:00", "Examen departamental", "Pendiente", "Salón 2103", "Profesor Gómez", "Un día antes")
        demo("Práctica", 2, "12:30", "Entrega de práctica en laboratorio", "Realizado", "Laboratorio 3", "María López", "Al momento")
        demo("Entrega de proyecto", 3, "16:00", "Entrega de avance del proyecto", "Aplazado", "Biblioteca", "Carlos Ramírez", "Un día antes")
        demo("Otro", 4, "18:00", "Cena con el equipo", "Pendiente", "Centro comercial", "Ana Torres", "Sin recordatorio")
        demo("Reunión", -3, "09:00", "Junta con asesor", "Realizado", "Sala de juntas", "Alejandro", "Al momento")
        demo("Examen", 12, "11:00", "Examen final", "Pendiente", "Salón 1201", "Profesor Gómez", "Un día antes")
    }

    fun nuevoId() = siguienteId++

    private fun demo(
        categoria: String, dias: Int, hora: String, descripcion: String,
        status: String, ubicacion: String, contacto: String, recordatorio: String
    ) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, dias)
        eventos.add(
            Evento(nuevoId(), categoria, Evento.FORMATO.format(cal.time), hora,
                descripcion, status, ubicacion, contacto, recordatorio)
        )
    }

    fun ordenados(lista: List<Evento>): List<Evento> =
        lista.sortedWith(compareBy<Evento>({ it.fechaDate() }, { it.hora }))

    fun proximos(dias: Int): List<Evento> {
        val inicio = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val fin = (inicio.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, dias) }
        return ordenados(eventos.filter {
            val d = it.fechaDate()
            d != null && !d.before(inicio.time) && !d.after(fin.time)
        })
    }
}
