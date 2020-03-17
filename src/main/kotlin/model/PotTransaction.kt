package model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.dao.id.EntityID

object PotTransactions : IntIdTable() {
    val potId = reference("potId", Pots)
    val userId = reference("userId", Users)
    val amount = double("amount")

    override val primaryKey = PrimaryKey(potId, userId)


}


class PotTransaction(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PotTransaction>(PotTransactions)

    var pot by Pot referencedOn PotTransactions.potId
    var user by User referencedOn PotTransactions.userId
    var amount by PotTransactions.amount
}


