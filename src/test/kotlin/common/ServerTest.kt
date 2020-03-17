package common

import io.ktor.http.*
import io.ktor.server.testing.*
import module
import org.amshove.kluent.*
import org.junit.jupiter.api.*

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

    private fun withServer(block: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }, block)
    }


}
