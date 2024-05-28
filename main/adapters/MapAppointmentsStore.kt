package org.example.adapters

import org.example.domain.AppointmentsStorePort
import org.example.domain.model.Appointment
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class MapAppointmentsStore : AppointmentsStorePort {
    private val map: ConcurrentMap<String, Appointment> = ConcurrentHashMap()

    override fun insert(appointment: Appointment) {
        map[appointment.id] = appointment
    }

    override fun delete(id: String) {
        map.remove(id)
    }

    override fun get(id: String): Appointment? =
        map[id]
}
