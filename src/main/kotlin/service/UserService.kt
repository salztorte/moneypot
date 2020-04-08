package service

import model.*
import service.DatabaseFactory.dbQuery

class UserService {
    suspend fun pots(user: User) = dbQuery { user.pots.toList() }
}
