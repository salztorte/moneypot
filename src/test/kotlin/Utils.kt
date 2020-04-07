import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.*

@KtorExperimentalAPI
fun withServer(block: TestApplicationEngine.() -> Unit) = withTestApplication({ module() }, block)

@KtorExperimentalAPI
fun loadTokenForUser(username: String = "test", password: String = "test", block: (String?) -> Unit) = withServer {
    handleRequest {
        method = HttpMethod.Post
        uri = "login"
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(json {
            "name" to username
            "password" to password
        }.toString())
    }.apply {
        block(response.content)
    }
}

@KtorExperimentalAPI
fun loadToken(block: (String?) -> Unit) = loadTokenForUser("test", "test", block)
