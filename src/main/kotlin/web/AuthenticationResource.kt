package web

import JwtConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.*
import service.*


data class LoginRequest(val name: String, val password: String): Credential

data class RegistrationRequest(val name: String, val password: String)

@KtorExperimentalAPI
fun Route.authentication() {
    val authenticationService: AuthenticationService by inject()

    post("/login") {
        try {
            val credentials = call.receive<LoginRequest>()
            val user = authenticationService.getUserForCredentials(credentials)

            if (user == null) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                val token = JwtConfig.makeToken(user)
                call.respondText(token)
            }
        }catch (e: MissingRequestParameterException){
            call.respond(HttpStatusCode.BadRequest, "The request data are wrong")
        }
    }

    post("/registration") {
        val regUser = call.receive<RegistrationRequest>()

        try {
            authenticationService.addNewUser(regUser)
            call.respond(HttpStatusCode.Created)
        }catch (ex: Exception) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
