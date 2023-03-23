package org.example.rest

import com.hexagonkt.core.ALL_INTERFACES
import com.hexagonkt.core.media.APPLICATION_JSON
import com.hexagonkt.core.require
import com.hexagonkt.http.model.ContentType
import com.hexagonkt.http.server.HttpServer
import com.hexagonkt.http.server.HttpServerSettings
import com.hexagonkt.http.server.handlers.HttpHandler
import com.hexagonkt.http.server.handlers.path
import com.hexagonkt.http.server.jetty.JettyServletAdapter
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.example.domain.AppointmentsService

class RestApi(
    private val appointmentsService: AppointmentsService,
    bindPort: Int,
) {
    private val appointmentsHandler: HttpHandler = path("/appointments") {
        post {
            val message = AppointmentMessage(request.bodyString())
            appointmentsService.create(message.appointment())
            ok(message)
        }

        get("/{id}") {
            val id = pathParameters.require("id")
            val appointment = appointmentsService.get(id)
            if (appointment == null)
                notFound(mapOf("id" to id))
            else
                ok(AppointmentMessage(appointment))
        }

        delete("/{id}") {
            val id = pathParameters.require("id")
            if (appointmentsService.delete(id))
                ok(mapOf("id" to id))
            else
                notFound(mapOf("id" to id))
        }
    }

    val applicationHandler: HttpHandler = path("/api") {
        exception<Exception> { internalServerError(it.message + " BOOM!") }
        after("*") { send(contentType = ContentType(APPLICATION_JSON)) }
        after("*") {
            send(
                body = when (val b = response.body) {
                    is Message -> b.data.serialize(Json)
                    is Map<*, *> -> b.serialize(Json)
                    is String -> b
                    else -> error("")
                }
            )
        }

        use(appointmentsHandler)
    }

    private val settings = HttpServerSettings(ALL_INTERFACES, bindPort)
    private val serverAdapter = JettyServletAdapter(minThreads = 4)
    val server: HttpServer = HttpServer(serverAdapter, applicationHandler, settings)
}
