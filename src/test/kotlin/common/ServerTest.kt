package common

import io.ktor.http.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

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
}



