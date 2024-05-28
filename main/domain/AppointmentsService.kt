package org.example.domain

import org.example.domain.model.Appointment

class AppointmentsService(
    private val store: AppointmentsStorePort,
    private val notifier: AppointmentsNotifierPort,
    private val createMessage: String,
    private val deleteMessage: String,
) {
    fun create(appointment: Appointment) {
        store.insert(appointment)
        notifier.notify(appointment.userIds, createMessage.format(appointment.start))
    }

    fun delete(id: String): Boolean {
        val appointment = store.get(id) ?: return false
        store.delete(id)
        notifier.notify(appointment.userIds, deleteMessage.format(appointment.start))
        return true
    }

    fun get(id: String): Appointment? =
        store.get(id)
}
