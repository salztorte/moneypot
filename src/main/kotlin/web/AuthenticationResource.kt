package web

import io.ktor.application.call
import io.ktor.auth.Credential
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import org.koin.ktor.ext.inject
import service.AuthenticationService


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
