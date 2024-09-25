package org.example.rest

import com.hexagontk.core.ALL_INTERFACES
import com.hexagontk.core.media.APPLICATION_JSON
import com.hexagontk.core.require
import com.hexagontk.http.model.ContentType
import com.hexagontk.http.server.HttpServer
import com.hexagontk.http.server.HttpServerSettings
import com.hexagontk.http.handlers.HttpHandler
import com.hexagontk.http.handlers.path
import com.hexagontk.http.server.netty.NettyServerAdapter
import com.hexagontk.serialization.jackson.json.Json
import com.hexagontk.serialization.serialize
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
    private val serverAdapter = NettyServerAdapter()
    val server: HttpServer = HttpServer(serverAdapter, applicationHandler, settings)
}
