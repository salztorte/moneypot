package model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*


object Pots : IntIdTable() {
    val name = varchar("name", 50)
    val owner = reference("owner", Users)
}

class Pot(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Pot>(Pots)

    var name by Pots.name
    var users by User via PotUsers
    var owner by User referencedOn Pots.owner
    val transactions by PotTransaction referrersOn PotTransactions.potId


    val sumOfAmount: Double
        get() = transactions.toList()
            .fold(0.0) { acc, potTransaction -> acc + potTransaction.amount }


    fun addUser(user: User): Pot {
        val list = users.toMutableList()
        list.add(user)
        users = SizedCollection(list)
        return this
    }

    fun addUser(userList: List<User>): Pot {
        val list = users.toMutableList()
        list.union(userList)
        users = SizedCollection(list)
        return this
    }


    data class Response(
        val id: Int,
        val name: String
        //val sum: Double,
        //val owner: User.Response
    ) {
        constructor(pot: Pot) : this(
            pot.id.value,
            pot.name
          //  pot.sumOfAmount,
//            User.Response(pot.owner)
        )
    }

}

