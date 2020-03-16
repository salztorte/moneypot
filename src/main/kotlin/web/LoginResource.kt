package web

import io.ktor.application.call
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import service.UserService

fun Route.login() {
    val userService: UserService by inject()

    post("login") {
        val credentials = call.receive<UserPasswordCredential>()

        val user = userService.getUserForCredentials(credentials);

        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized)
        }else {
            val token = JwtConfig.makeToken(user)
            call.respondText(token)
        }

    }

}
