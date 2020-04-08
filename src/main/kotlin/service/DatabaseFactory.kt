package service

import com.zaxxer.hikari.*
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
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


    object TestDb {
        fun create() {
            val user1 = newUser("test", "test")
            val user2 = newUser("test2", "test")

            newPot("pot1", user1)
            newPot("pot2", user1)
            val pot = newPot("pot3", user2)
            transaction {
                pot.addUser(user1)
            }
            newPot("pot4", user2)
            newPot("pot5", user2)


//
//            transaction {
//                PotTransaction.new {
//                    this.pot = pot
//                    this.user = user
//                    this.amount = 5.5
//
//                }
//            }
        }

        fun clean() {
            transaction {
                drop(*tables)
                create(*tables)
            }
        }


        private fun newUser(name: String, password: String) = transaction {
            val user = User.new { this.name = name }

            SecureUser.new {
                userId = user.id.value
                loginName = name
                this.password = password
            }

            user
        }

        private fun newPot(name: String, user: User) = transaction {
            val pot = Pot.new {
                this.name = name
                owner = user
            }

            pot.addUser(user)
        }


    }


}

