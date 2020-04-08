package service

import model.*
import service.DatabaseFactory.dbQuery

class PotService {
    suspend fun new(name: String, user: User) = dbQuery {
        Pot.new {
            this.name = name
            this.owner = user
        }
    }

    suspend fun addUserToPot(pot: Pot, user: User) {
        dbQuery {
            pot.addUser(user)
        }
    }

    suspend fun all() = dbQuery { Pot.all().toList() }

    suspend fun ownedPots(user: User): List<Pot> {
        return dbQuery { Pot.find { Pots.owner eq user.id }.toList() }
    }

    suspend fun getPotsForUser(user: User): List<Pots> {
        TODO()
    }
}
