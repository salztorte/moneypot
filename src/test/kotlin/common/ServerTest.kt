package common

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

@KtorExperimentalAPI
open class ServerTest {
    @Test
    fun `health_check is ok`() = withServer {
        handleRequest {
            method = HttpMethod.Get
            uri = "/health_check"
        }.apply {
            requestHandled shouldBe true
            response.status() shouldEqual HttpStatusCode.OK
            response.content shouldEqual "OK"
        }
    }

    @Test
    fun `health_check return input`() = withServer {
        val testData = "test"

        handleRequest {
            method = HttpMethod.Post
            uri = "/health_check"

            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json { "test" to testData }.toString())
        }.apply {
            response.status() shouldEqual HttpStatusCode.OK
            response.content shouldEqual testData
        }
    }
}



