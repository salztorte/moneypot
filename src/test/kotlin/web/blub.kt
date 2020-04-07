package web

import io.ktor.http.*
import io.ktor.util.*
import loadToken
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

@KtorExperimentalAPI
class Blub {

    private var token: String = "";

    @BeforeEach
    fun beforeEach() = loadToken { token = it!! }

    @Test
    fun fuubar() = withServer {

        handleRequest {
            method = HttpMethod.Get
            uri = "secret"
            addHeader(HttpHeaders.Authorization, "Bearer ${token}")
        }.apply {
            response.status() shouldEqual HttpStatusCode.OK
        }

    }
}