package service

import io.ktor.auth.UserPasswordCredential
import model.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import web.LoginRequest
import web.RegisterRequest

class AuthenticationService {

    fun getUserForCredentials(credentials: LoginRequest): User? {
        transaction {
            val secureUser = SecureUser.find {
                (SecureUsers.loginName eq credentials.name) and (SecureUsers.password eq credentials.password)
            }

            if(secureUser.empty()) {
                return@transaction null
            }

           User.findById(secureUser.toList().first().userId)
        }.apply {
            return this
        }
    }

    fun addNewUser(credentials: RegisterRequest) {
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
