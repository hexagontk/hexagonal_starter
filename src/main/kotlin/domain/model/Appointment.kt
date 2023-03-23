package org.example.domain.model

import java.time.LocalDateTime

data class Appointment(
    val id: String,
    val userIds: List<String>,
    val start: LocalDateTime,
    val end: LocalDateTime,
)
