import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import io.ktor.websocket.WebSockets
import model.*
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import service.DatabaseFactory
import service.DatabaseFactory.dbQuery

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Koin) {
        modules(
                module(createdAtStart = true) {
                }
        )
    }
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    DatabaseFactory.init()



    routing {
        get("/") {
            transaction {
                Pot.all().map { PotJson(it)}
            }.apply {
                call.respond(this)
            }
        }

    }

}

class MainApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
        }
    }
}
