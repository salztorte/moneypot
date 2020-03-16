import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import model.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.slf4j.LoggerFactory
import service.DatabaseFactory

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Koin) {
        modules(
                module(createdAtStart = true) {
                    single {}
                }
        )
    }
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    val logger = LoggerFactory.getLogger(Application::class.java)
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


                logger.debug("${id}")
                transaction {
                    val user = User.findById(id)
                    logger.debug(user?.name)
                    user
                }
            }
        }
    }
    DatabaseFactory.init()


    install(Routing) {

        get("/") {
            transaction {
                User.all().toList().map { User.Json(it) }
            }.apply {
                call.respond(this)
            }
        }

        /**
         * A public login [Route] used to obtain JWTs
         */
        post("login") {
            val credentials = call.receive<UserPasswordCredential>()
            transaction {
                val secureUser = SecureUser.find{
                    (SecureUsers.loginName eq credentials.name) and (SecureUsers.password eq credentials.password)
                }.toList().first()
                JwtConfig.makeToken(secureUser)
            }.apply {
                call.respondText(this)
            }


        }

        /**
         * All [Route]s in the authentication block are secured.
         */
        authenticate {
            route("secret") {
                get {
                    val user = call.user!!
                    call.respond(user.name)
                }
            }
        }
    }
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
