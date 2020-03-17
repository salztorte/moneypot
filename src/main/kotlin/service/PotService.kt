package service

import model.*
import service.DatabaseFactory.dbQuery

class PotService {
    suspend fun new(name: String, user: User) {
        dbQuery {
            Pot.new {
                this.name = name
                this.owner = user
            }
        }
    }

    suspend fun addUserToPot(pot: Pot, user: User) {
        dbQuery {
            pot.addUser(user)
        }
    }
}
