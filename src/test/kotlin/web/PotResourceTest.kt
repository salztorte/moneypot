package web

import com.fasterxml.jackson.module.kotlin.*
import extensions.*
import io.ktor.http.*
import io.ktor.util.*
import model.*
import org.amshove.kluent.*
import org.junit.jupiter.api.*
import withServer

@KtorExperimentalAPI
class PotResourceTest {

    @Test
    fun getAllPots() = withServer {
        handleRequestWithToken {
            method = HttpMethod.Get
            uri = "/pots/all"
        }.apply {
            val pots = jacksonObjectMapper().readValue<List<Pot.Response>>(response.content ?: "[]")

            response.status() shouldEqual HttpStatusCode.OK
            pots.count() shouldEqual 5
            pots.first().name shouldEqual "pot1"
            pots.first().id shouldEqual 1
        }
    }

    @Test
    fun getPotsForOwner() = withServer {
        handleRequestWithToken {
            method = HttpMethod.Get
            uri = "/pots/owed"
        }.apply {
            val pots = jacksonObjectMapper().readValue<List<Pot.Response>>(response.content ?: "[]")
            response.status() shouldEqual HttpStatusCode.OK
            pots.count() `should be equal to` 2
            pots[0].name shouldEqual "pot1"
            pots[1].name shouldEqual "pot2"
        }
    }

    @Test
    fun `get pots where user is in`() = withServer {
        handleRequestWithToken {
            method = HttpMethod.Get
            uri = "/pots"
        }.apply {
            val pots = jacksonObjectMapper().readValue<List<Pot.Response>>(response.content ?: "[]")
            response.status() shouldEqual HttpStatusCode.OK
            pots.count() `should be equal to` 2
            pots[0].name shouldEqual "pot1"
            pots[1].name shouldEqual "pot2"
            pots[2].name shouldEqual "pot3"
        }
    }

}