package org.example

import com.hexagontk.core.Platform
import org.example.adapters.LoggingAppointmentsNotifier
import org.example.adapters.MapAppointmentsStore
import org.example.domain.AppointmentsService
import org.example.rest.RestApi

lateinit var restApi: RestApi

fun main() {
    restApi = RestApi(
        AppointmentsService(
            MapAppointmentsStore(),
            LoggingAppointmentsNotifier(),
            Platform.systemSettingOrNull("CREATE_MESSAGE") ?: "Make room for an appointment at %s",
            Platform.systemSettingOrNull("DELETE_MESSAGE") ?: "You are free at %s, go have fun!",
        ),
        Platform.systemSettingOrNull("BIND_PORT") ?: 9090
    )

    restApi.server.start()
}
