package model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

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


