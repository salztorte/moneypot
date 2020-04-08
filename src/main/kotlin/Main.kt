import com.fasterxml.jackson.databind.*
import extensions.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.ContentType.Text.Plain
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import io.ktor.websocket.*
import model.*
import org.jetbrains.exposed.sql.transactions.*
import org.koin.dsl.*
import org.koin.ktor.ext.*
import service.*
import web.*

@KtorExperimentalAPI
fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(StatusPages) {
        exception<NotImplementedError> { call.respond(HttpStatusCode.NotImplemented) }

        exception<NotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
//        status(HttpStatusCode.NotFound) {
//            val content = TextContent("${it.value} ${it.description}", Plain.withCharset(Charsets.UTF_8), it)
//            call.respond(HttpStatusCode.NotFound, content)
//        }

        exception<Throwable> { call.respond(HttpStatusCode.InternalServerError) }
    }
    install(Koin) {
        modules(
                module(createdAtStart = true) {
                    single { AuthenticationService() }
                    single { PotService() }
                    single { UserService() }
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
        healthCheck()


        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            pots()

            route("secret") {
                get {
                    val user = call. user!!
                    call.respond(User.Response(user))
                }
            }
        }



    }

    DatabaseFactory.init()
}

class MainApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
        }
    }
}

