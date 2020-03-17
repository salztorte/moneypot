package model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.dao.id.EntityID


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
        get() = transactions.toList().fold(0.0) { acc, potTransaction -> acc + potTransaction.amount }

    data class Json(
            val id: Int,
            val name: String,
            val users: List<User.Json>,
            val sum: Double,
            val owner: User.Json
    ) {
        constructor(pot: Pot) : this(pot.id.value,
                pot.name,
                pot.users.map { User.Json(it) },
                pot.sumOfAmount,
                User.Json(pot.owner)
        )
    }

}

