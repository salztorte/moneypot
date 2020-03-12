package model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table


object Users : IntIdTable() {
    val name = varchar("name", 50)
}


class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var pots by Pot via PotUsers

}


data class UserJson(
        val id: Int,
        val name: String
) {
    constructor(user: User) : this(user.id.value, user.name)
}
