package service

import io.ktor.auth.*
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import web.*

class AuthenticationService {

    /**
     *
     * @param credentials LoginRequest
     * @return User?
     */
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

    /**
     *
     * @param credentials UserPasswordCredential
     */
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
