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
            create(Pots, Users, PotUsers, PotTransactions)
        }

        val user = transaction {
            User.new {
                displayName = "test"
            }
        }

        val user2 = transaction {
            User.new {
                displayName = "test2"
            }
        }

        val pot = transaction {
            Pot.new {
                name = "pot1"
            }
        }


        transaction {
            pot.users = SizedCollection(listOf(user, user2))
        }

        transaction {
            PotTransaction.new {
                setPot(pot)
                setUser(user)
                this.amount = 5.5

            }
            PotTransaction.new {
                setPot(pot)
                setUser(user2)
                this.amount = 10.0

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
