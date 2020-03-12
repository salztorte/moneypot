package model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Table


object PotUsers : Table() {
    val pot = reference("pot", Pots)
    val user = reference("user", Users)

    override val primaryKey = PrimaryKey(pot, user)
}

