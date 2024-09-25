package org.example

import com.hexagontk.core.logging.info
import com.hexagontk.core.media.APPLICATION_JSON
import com.hexagontk.core.urlOf
import com.hexagontk.http.client.HttpClient
import com.hexagontk.http.client.HttpClientSettings
import com.hexagontk.http.client.jetty.JettyClientAdapter
import com.hexagontk.http.model.HttpMethod.POST
import com.hexagontk.http.model.NOT_FOUND_404
import com.hexagontk.http.model.OK_200
import com.hexagontk.http.model.HttpRequest
import com.hexagontk.serialization.jackson.json.Json
import com.hexagontk.serialization.parseMap
import com.hexagontk.serialization.serialize
import org.example.domain.model.Appointment
import org.example.rest.AppointmentMessage
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URL
import java.time.LocalDateTime
import kotlin.test.assertEquals

@TestInstance(PER_CLASS)
internal class ApplicationTest {

    private val baseUrl: URL by lazy { urlOf("http://localhost:${restApi.server.runtimePort}") }
    private val settings: HttpClientSettings by lazy { HttpClientSettings(baseUrl) }
    private val client: HttpClient by lazy { HttpClient(JettyClientAdapter(), settings) }

    @BeforeAll fun beforeAll() {
        main()
        client.start()
    }

    @AfterAll fun afterAll() {
        client.stop()
        restApi.server.stop()
    }

    @Test fun `HTTP router returns proper status, headers and body`() {
        val now = LocalDateTime.now()
        val appointment = Appointment("1", listOf("mike", "jill"), now, now.plusHours(1))
        val appointmentMessage = AppointmentMessage(appointment)
        val body = appointmentMessage.data.serialize(Json)
        val handler = restApi.applicationHandler

        handler.process(HttpRequest(POST, path="/api/appointments", body = body)).apply {
            assertEquals(OK_200, status)
            assertEquals(APPLICATION_JSON, response.contentType?.mediaType)
        }
    }

    @Test fun `HTTP request returns proper status, headers and body`() {
        val now = LocalDateTime.now()
        val appointment = Appointment("1", listOf("mike", "jill"), now, now.plusHours(1))
        val appointmentMessage = AppointmentMessage(appointment)
        val body = appointmentMessage.data.serialize(Json)

        client.post("/api/appointments", body).apply {
            bodyString().info()
            assertEquals(OK_200, status)
            assertEquals(APPLICATION_JSON, contentType?.mediaType)
        }

        client.get("/api/appointments/1").apply {
            bodyString().info()
            assertEquals("1", bodyString().parseMap(Json)["id"])
            assertEquals(OK_200, status)
            assertEquals(APPLICATION_JSON, contentType?.mediaType)
        }

        client.delete("/api/appointments/1").apply {
            bodyString().info()
            assertEquals("1", bodyString().parseMap(Json)["id"])
            assertEquals(OK_200, status)
            assertEquals(APPLICATION_JSON, contentType?.mediaType)
        }

        client.get("/api/appointments/1").apply {
            bodyString().info()
            assertEquals(NOT_FOUND_404, status)
            assertEquals(APPLICATION_JSON, contentType?.mediaType)
        }

        client.delete("/api/appointments/1").apply {
            bodyString().info()
            assertEquals(NOT_FOUND_404, status)
            assertEquals(APPLICATION_JSON, contentType?.mediaType)
        }
    }
}
