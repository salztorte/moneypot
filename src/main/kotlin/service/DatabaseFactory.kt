package service

import com.zaxxer.hikari.*
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {

    private val tables = arrayOf(Pots, Users, PotUsers, PotTransactions, SecureUsers)

    fun init() {
        Database.connect(hikari())

        transaction {
            create(*tables)
        }
    }

    fun createTestDb() {
        val user = transaction {
            User.new {
                name = "tunin"
            }
        }


        val secureUser1 = transaction {
            SecureUser.new {
                userId = user.id.value
                loginName = user.name
                password = "test"
            }
        }


        val pot = transaction {
            Pot.new {
                name = "pot1"
                owner = user
            }
        }


        transaction {
            pot.addUser(user)
        }

        transaction {
            PotTransaction.new {
                this.pot = pot
                this.user = user
                this.amount = 5.5

            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
}
