package com.shen.database

import com.google.gson.Gson
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import util.DatabaseHelper
import java.lang.Exception

object DatabaseFactory : DatabaseHelper {
    private val gson: Gson = Gson()

    fun init(@NotNull table: List<Table>) {
        Database.connect(dbConnect())
        transaction {
            table.forEach {
                when (it) {
                    is UsersTable -> {
                        create(it)
                        update(it)
                    }

                    is SchoolInfoTable -> {
                        create(it)
                        update(it)
                    }

                    is SchoolGuideTimeTable -> {
                        create(it)
                        update(it)
                    }
                }

//                if (it !is UsersTable) {
//                    update(it)
//                }
            }
        }

    }

    override fun update(table: Table) {
        when (table) {
            is UsersTable -> {
                try {
                    table.insert {
                        it[phone_number] = 123456
                        it[password] = "aaaa"
                        it[name] = "name"
                        it[user_avatar] = "avatar"
                        it[user_school] = "school"
                        it[user_college] = "college"
                        it[user_name] = "user_name"
                        it[user_id_card] = "id_card"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            is SchoolInfoTable -> {

            }

            is SchoolGuideTimeTable -> {

            }
        }
    }


    private fun dbConnect(): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "org.h2.Driver"
        hikariConfig.jdbcUrl = "jdbc:h2:~/dick"
        hikariConfig.username = "shen"
        hikariConfig.password = "1234"
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }
}