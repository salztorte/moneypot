package service

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())

        transaction {
            create(Pots, Users, PotUsers, PotTransactions, SecureUsers)
        }

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
            }
        }


        transaction {
            pot.users = SizedCollection(listOf(user))
        }

        transaction {
            PotTransaction.new {
                setPot(pot)
                setUser(user)
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
