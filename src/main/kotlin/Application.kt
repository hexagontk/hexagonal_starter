package org.example

import com.hexagontk.core.Jvm
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
            Jvm.systemSettingOrNull("CREATE_MESSAGE") ?: "Make room for an appointment at %s",
            Jvm.systemSettingOrNull("DELETE_MESSAGE") ?: "You are free at %s, go have fun!",
        ),
        Jvm.systemSettingOrNull("BIND_PORT") ?: 9090
    )

    restApi.server.start()
}
