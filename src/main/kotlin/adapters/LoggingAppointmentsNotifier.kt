package org.example.adapters

import com.hexagontk.core.info
import com.hexagontk.core.loggerOf
import org.example.domain.AppointmentsNotifierPort

class LoggingAppointmentsNotifier : AppointmentsNotifierPort {
    private val logger = loggerOf(this::class)

    override fun notify(userIds: Collection<String>, message: String) {
        userIds.forEach {
            logger.info { "$it $message"}
        }
    }
}
