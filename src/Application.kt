package com.shen

import com.shen.database.DatabaseFactory
import com.shen.database.SchoolGuideTimeTable
import com.shen.database.SchoolInfoTable
import com.shen.database.UsersTable
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class test(
    val phone: Int,
    val password: String
)
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val tableList = listOf(
        UsersTable,
        SchoolInfoTable,
        SchoolGuideTimeTable
    )
    val usersTable = tableList[0] as UsersTable
    val phonePSW = Collections.synchronizedMap(mutableMapOf<String, test>())
    install(Authentication) {
        form("login") {
            userParamName = "phone_number"
            passwordParamName = "password"
            challenge = FormAuthChallenge.Unauthorized
            validate { credentials ->
                usersTable.selectAll().forEach {
                    phonePSW[it[UsersTable.name]] = test(it[UsersTable.phone_number], it[UsersTable.password])
                }

                if (phonePSW[credentials.name]?.password == credentials.password)
                    UserIdPrincipal(credentials.name)
                else
                    null
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    DatabaseFactory.init(table = tableList)

    routing {
        route("/login") {
            authenticate("login") {
                post {
                    val principal = call.principal<UserIdPrincipal>()
                    if (principal == null) {
                        call.respond(mapOf("error" to "No principal"))
                    } else {
                        call.respond("true")
                    }
                }
            }
        }
    }
}


