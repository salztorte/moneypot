package web

import extensions.*
import io.ktor.http.*
import io.ktor.util.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

@KtorExperimentalAPI
class Blub {

    @Test
    fun fuubar() = withServer {
        handleRequestWithToken {
            method = HttpMethod.Get
            uri = "secret"
        }.apply {
            response.status() shouldEqual HttpStatusCode.OK
        }
    }
}