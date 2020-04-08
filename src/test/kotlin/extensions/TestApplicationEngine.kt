package extensions

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.*

fun TestApplicationEngine.loadTokenForUser(username: String, password: String): String {
    var token = ""
    handleRequest {
        method = HttpMethod.Post
        uri = "login"
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(json {
            "name" to username
            "password" to password
        }.toString())
    }.apply {
        token = response.content!!
    }
    return token
}

fun TestApplicationEngine.handleRequestWithToken(username: String = "test", password: String = "test", block: TestApplicationRequest.() -> Unit) = handleRequest {
    val token = loadTokenForUser(username, password)
    addHeader(HttpHeaders.Authorization, "Bearer ${token}")
    block()
}