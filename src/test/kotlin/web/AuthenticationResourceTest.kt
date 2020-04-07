package web

import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

@KtorExperimentalAPI
class AuthenticationResourceTest {
    @Test
    fun `test user can login`() = withServer {
        handleRequest {
            method = HttpMethod.Post
            uri = "login"
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json{
                "name" to "test"
                "password" to "test"
            }.toString())
        }.apply {
            response.status() shouldEqual HttpStatusCode.OK
        }
    }

    @Test
    fun `wrong password`() = withServer {

        handleRequest {
            method = HttpMethod.Post
            uri = "login"
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json{
                "name" to "test"
                "password" to "qwreqwe"
            }.toString())
        }.apply {
            response.status() shouldEqual HttpStatusCode.Forbidden

        }
    }

    @Test
    fun `register new user`() = withServer {
        handleRequest {
            method = HttpMethod.Post
            uri = "registration"
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(json{
                "name" to "registraionTest"
                "password" to "lampe1"
            }.toString())
        }.apply {
            response.status() shouldEqual HttpStatusCode.Created
        }
    }

}




