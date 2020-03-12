package model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Pots : IntIdTable() {
    val name = varchar("name", 50)

}

class Pot(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Pot>(Pots)

    var name by Pots.name
    var users by User via PotUsers
    val transactions by PotTransaction referrersOn PotTransactions.potId


    val summOfAmount: Double
        get() = transactions.toList().fold(0.0) { acc, potTransaction -> acc + potTransaction.amount }


}


data class PotJson(
        val id: Int,
        val name: String,
        val users: List<UserJson>
) {
    constructor(pot: Pot) : this(pot.id.value, pot.name, pot.users.map { UserJson(it) })
}
