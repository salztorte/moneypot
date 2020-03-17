package web

import JwtConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.*
import service.*


@KtorExperimentalLocationsAPI
@Location("login")
data class LoginRequest(val name: String, val password: String): Credential


@KtorExperimentalLocationsAPI
fun Route.authentication() {
    val authenticationService: AuthenticationService by inject()

    post<LoginRequest> {
        val credentials = call.receive<LoginRequest>()

        val user = authenticationService.getUserForCredentials(credentials);

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized)
        }else {
            val token = JwtConfig.makeToken(user)
            call.respondText(token)
        }

    }
}
