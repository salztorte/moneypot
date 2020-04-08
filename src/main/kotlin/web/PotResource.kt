package web

import extensions.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import org.jetbrains.exposed.sql.*
import org.koin.ktor.ext.*
import service.*

fun Route.pots() {
    val potService: PotService by inject()



    get("/pots/all") {
        val pots = potService.all().map { Pot.Response(it) }

        call.respond(pots)
    }

    get("/pots/owed") {
        val pots = potService.ownedPots(call.user!!).map { Pot.Response(it) }

        call.respond(pots)
    }

    get("/pots") {
        val user = call.user!!;
        val pots = user.pots.toMutableList()


        call.respond(pots.map { Pot.Response(it) })
    }

}