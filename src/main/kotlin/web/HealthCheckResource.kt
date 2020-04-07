package web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

data class HealthCheckPost(val test: String)

fun Route.healthCheck() {
    get("/health_check") {
        call.respondText("OK")
    }


    post("/health_check") {
        val testObj =  call.receive<HealthCheckPost>()

        call.respondText(testObj.test)
    }
}