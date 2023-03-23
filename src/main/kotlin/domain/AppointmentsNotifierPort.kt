package org.example.domain

interface AppointmentsNotifierPort {
    fun notify(userIds: Collection<String>, message: String)
}
