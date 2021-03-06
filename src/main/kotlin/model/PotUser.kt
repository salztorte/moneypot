package model

import org.jetbrains.exposed.sql.*


object PotUsers : Table() {
    val pot = reference("pot", Pots)
    val user = reference("user", Users)

    override val primaryKey = PrimaryKey(pot, user)
}

