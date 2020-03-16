package service

import io.ktor.auth.UserPasswordCredential
import model.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {

    fun getUserForCredentials(credentials: UserPasswordCredential): User? {
        transaction {
            val secureUser = SecureUser.find {
                (SecureUsers.loginName eq credentials.name) and (SecureUsers.password eq credentials.password)
            }

            if(secureUser.empty()) {
                return@transaction null
            }

           User.findById(secureUser.toList().first().id)
        }.apply {
            return this
        }
    }

    fun addNewUser(credentials: UserPasswordCredential) {
        transaction {
            val user = User.new {
                name = credentials.name
            }

            SecureUser.new {
                userId = user.id.value
                loginName = user.name
                password = credentials.password
            }
        }
    }
}
