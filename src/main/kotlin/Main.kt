import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import model.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.slf4j.LoggerFactory
import service.DatabaseFactory
import service.AuthenticationService
import web.authentication

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Locations)
    install(Koin) {
        modules(
                module(createdAtStart = true) {
                    single { AuthenticationService() }
                }
        )
    }
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in [Routing].
         * If the token is valid, the corresponding [User] is fetched from the database.
         * The [User] can then be accessed in each [ApplicationCall].
         */
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
                val id = it.payload.getClaim("id").asInt()

                transaction {
                    User.findById(id)
                }
            }
        }
    }


    install(Routing) {
        authentication()

        get("/") {
            transaction {
                User.all().toList().map { User.Json(it) }
            }.apply {
                call.respond(this)
            }
        }

        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            route("secret") {
                get {
                    val user = call.user!!
                    call.respond(User.Json(user))
                }
            }
        }
    }

    DatabaseFactory.init()
    DatabaseFactory.createTestDb()
}

val ApplicationCall.user get() = authentication.principal<User>()


class MainApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
        }
    }
}

