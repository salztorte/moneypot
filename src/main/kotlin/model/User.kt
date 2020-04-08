package model

import io.ktor.auth.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.dao.id.EntityID


object Users : IntIdTable() {
    val name = varchar("display_name", 50)
}

class User(id: EntityID<Int>) : IntEntity(id), Principal {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var pots by Pot via PotUsers

    data class Response(
        val id: Int,
        val name: String
    ) {
        constructor(user: User) : this(user.id.value, user.name)
    }

}

