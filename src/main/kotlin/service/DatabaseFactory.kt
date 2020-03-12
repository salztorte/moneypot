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
            create(Pots, Users, PotUsers)
        }

        val user = transaction {
            User.new {
                name = "test"
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
