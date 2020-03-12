package model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction



object Pots : IntIdTable() {
    val name = varchar("name", 50)
}

class Pot(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Pot>(Pots)

    var name by Pots.name
    var users by User via PotUsers
}


data class PotJson(
        val id: Int,
        val name: String,
        val users: List<UserJson>
) {
    constructor(pot: Pot): this(pot.id.value, pot.name, pot.users.map { UserJson(it) })
}
