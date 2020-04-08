package extensions

import io.ktor.application.*
import io.ktor.auth.*
import model.*

val ApplicationCall.user get() = authentication.principal<User>()