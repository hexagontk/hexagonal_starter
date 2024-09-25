package org.example.adapters

import com.hexagontk.core.logging.Logger
import org.example.domain.AppointmentsNotifierPort

class LoggingAppointmentsNotifier : AppointmentsNotifierPort {
    private val logger = Logger(this::class)

    override fun notify(userIds: Collection<String>, message: String) {
        userIds.forEach {
            logger.info { "$it $message"}
        }
    }
}
