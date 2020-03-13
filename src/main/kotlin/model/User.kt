package model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*


object Users : UUIDTable() {
    val displayName = varchar("display_name", 50)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    var displayName by Users.displayName
    var pots by Pot via PotUsers


    data class Json(
            val id: UUID,
            val displayName: String
    ) {
        constructor(user: User) : this(user.id.value, user.displayName)
    }

}

