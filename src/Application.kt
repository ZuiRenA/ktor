package com.shen

import com.shen.database.*
import com.shen.model.*
import com.shen.network.MessageUtil
import com.shen.util.DatabaseUtil
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.apache.http.HttpHost
import javax.xml.crypto.Data

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val tableList = listOf(
        UsersTable,
        SchoolInfoTable,
        SchoolGuideTimeTable,
        SchoolDormitoryTable
    )
    val client = HttpClient(engineFactory = Apache) {
        engine {
            followRedirects = true
            socketTimeout = 10_000
            connectTimeout = 10_000
            connectionRequestTimeout = 20_000

            customizeClient {
                setProxy(HttpHost("127.0.0.1", 8080))
                setMaxConnTotal(1000)
                setMaxConnPerRoute(100)
            }
            customizeRequest {  }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    DatabaseFactory.init(table = tableList)

    routing {
        post("/login") {
            val keyAndValue = call.receive<PandP>()
            val userList: List<Users>? = DatabaseFactory.selectAll(UsersTable) as List<Users>
            userList?.let {
                var hasPhoneNumber = false
                var index = -1
                userList.forEachIndexed { currentIndex, user ->
                    if (user.phone_number == keyAndValue.phone_number) {
                        index = currentIndex
                        hasPhoneNumber = true
                    } else {
                        if (!hasPhoneNumber)
                            hasPhoneNumber = false
                    }
                }

                if (hasPhoneNumber) {
                    if (userList[index].password == keyAndValue.password)
                        call.respond(isSuccess(isSuccess = true, respond = userList[index]))
                    else
                        call.respond(isSuccess(isSuccess = false, errorReason = "密码错误"))
                } else
                    call.respond(isSuccess(isSuccess = false, errorReason = "手机号错误"))
            }
        }
        post("/register") {
            val registerInfo = call.receive<Users>()
            val userList: List<Users>? = DatabaseFactory.selectAll(UsersTable) as List<Users>
            userList?.let {
                var hasPhoneNumber = false
                userList.forEach { user ->
                    if (user.phone_number == registerInfo.phone_number)
                        hasPhoneNumber = true
                    else {
                        if (!hasPhoneNumber)
                            hasPhoneNumber = false
                    }
                }
                if (hasPhoneNumber)
                    call.respond(isSuccess(isSuccess = false, errorReason = "手机号已存在"))
                else {
                    if (DatabaseFactory.insert(UsersTable, registerInfo).isSuccess) {
                        call.respond(DatabaseFactory.select(UsersTable, registerInfo.phone_number))
                    }
                }
            }
        }
        post("/school_guide_time") {
            val content = call.receive<Content>()
            call.respond(DatabaseFactory.select(SchoolGuideTimeTable, content.text))
        }
        post("/school_dormitory") {
            val id = call.receive<Id>()
            call.respond(DatabaseFactory.select(SchoolDormitoryTable, id.id))
        }
        post("/personData") {
            val users = call.receive<Users>()
            call.respond(DatabaseUtil.uploadPersonData(users))
        }
        get("/schoolInfo/{id}") {
            val param = call.parameters["id"]!!
            call.respond(DatabaseFactory.select(SchoolInfoTable, param.toInt()))
        }
        get("/school") {
            call.respond(DatabaseUtil.selectAllSchool())
        }
        get("/college/{id}") {
            val param = call.parameters["id"]!!
            call.respond(DatabaseUtil.selectAllCollege(param.toInt()))
        }
        post("/upload/letter") {
            val letter = call.receive<Letter>()
            call.respond(DatabaseUtil.uploadCALetter(letter))
        }
        get("/guideTime/{id}") {
            val param = call.parameters["id"]!!
            call.respond(DatabaseUtil.selectGuideTime(param.toInt()))
        }
        get("/message/{phone}") {
            val phone = call.parameters["phone"]
            call.respond(MessageUtil(phone).sendMessage())
        }
        post("/password") {
            val password = call.receive<Password>()
            call.respond(DatabaseUtil.changePassword(password = password))
        }

        get("/dormitory/{id}") {
            val id = call.parameters["id"]
            call.respond(DatabaseUtil.selectDormitory(id?.toInt()))
        }

        post("/select/dormitory") {
            val selectDor = call.receive<SelectDor>()
            call.respond(DatabaseUtil.studentSelectDor(selectDor))
        }

        get("/userTable") {
            call.respond(isSuccess(true, DatabaseFactory.selectAll(UsersTable)))
        }
    }
}


