package org.example.domain

import org.example.domain.model.Appointment

interface AppointmentsStorePort {
    fun insert(appointment: Appointment)
    fun delete(id: String)
    fun get(id: String): Appointment?
}
