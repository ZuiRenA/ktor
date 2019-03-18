package com.shen

import com.shen.database.*
import com.shen.model.*
import com.shen.util.DatabaseUtil
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing

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
                else
                    call.respond(DatabaseFactory.insert(UsersTable, registerInfo))
            }
        }

        post("/schoolInfo") {
            val id = call.receive<Id>()
            call.respond(DatabaseFactory.select(SchoolInfoTable, id.id))
        }

        post("/school_guide_time") {
            val content = call.receive<Content>()
            call.respond(DatabaseFactory.select(SchoolGuideTimeTable, content.text))
        }

        post("/school_dormitory") {
            val id = call.receive<Id>()
            call.respond(DatabaseFactory.select(SchoolDormitoryTable, id.id))
        }

        post("/upload") {
            val upload = call.receive<Upload>()
            call.respond(DatabaseUtil.uploadAvatar(upload))
        }
    }
}


