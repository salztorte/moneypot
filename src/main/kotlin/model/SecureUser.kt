package model

import io.ktor.auth.Principal
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.dao.id.EntityID

object SecureUsers: IntIdTable() {
    val userId = integer("user_id")
    val loginName = varchar("login_name", 50)
    val password = varchar("password", 50)

//    override val primaryKey = PrimaryKey(userId, loginName)
}


class SecureUser(id: EntityID<Int>): IntEntity(id), Principal {
    companion object: IntEntityClass<SecureUser>(SecureUsers)

    var userId by SecureUsers.userId
    var loginName by SecureUsers.loginName
    var password by SecureUsers.password
}
