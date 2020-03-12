package model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object PotTransactions: IntIdTable() {
    val potId = reference("pot", Pots)
    val userId = reference("user", Users)
    val amount = double("amount")
}


class PotTransaction(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PotTransaction>(PotTransactions)

    var potId by PotTransactions.potId
    var userId by PotTransactions.userId
    var amount by PotTransactions.amount
}


