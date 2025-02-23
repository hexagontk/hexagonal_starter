package org.example.rest

import com.hexagonkt.core.fieldsMapOf
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.parseMap
import org.example.domain.model.Appointment
import java.time.LocalDateTime

data class AppointmentMessage(override val data: Map<String, *>) : Message {
    private val id: String by data
    private val userIds: List<String> by data
    private val start: String by data
    private val end: String by data

    constructor(json: String) : this(json.parseMap(Json).mapKeys { it.key.toString() })

    constructor(appointment: Appointment) : this(
        fieldsMapOf(
            AppointmentMessage::id to appointment.id,
            AppointmentMessage::userIds to appointment.userIds,
            AppointmentMessage::start to appointment.start.toString(),
            AppointmentMessage::end to appointment.end.toString(),
        )
    )

    fun appointment(): Appointment =
        Appointment(id, userIds, LocalDateTime.parse(start), LocalDateTime.parse(end))
}
