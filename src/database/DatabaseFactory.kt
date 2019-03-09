package com.shen.database

import com.google.gson.Gson
import com.shen.model.*
import com.shen.model.SchoolDormitory
import com.shen.model.SchoolGuideTime
import com.shen.model.SchoolInfo
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import util.DatabaseHelper


object DatabaseFactory : DatabaseHelper {
    private val gson: Gson = Gson()

    fun init(@NotNull table: List<Table>) {
        Database.connect(dbConnect())
        transaction {
            table.forEach {
                create(it)
                initContent(it)
            }
        }
    }

    override fun initContent(table: Table) {
        when (table) {
            is UsersTable -> {
                try {
                    table.insert {
                        it[phone_number] = 13405665741
                        it[password] = "aaaa"
                        it[name] = "name"
                        it[user_avatar] = "avatar"
                        it[user_school] = "school"
                        it[user_college] = "college"
                        it[user_name] = "user_name"
                        it[user_id_card] = "id_card"
                        it[user_dormitory] = "dormitory"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            is SchoolInfoTable -> {
                try {
                    table.insert {
                        it[school_name] = "南宁学院"
                        it[school_address] = "龙亭路8号南宁学院"
                        it[school_introduce] = "这是学校简介"
                        it[school_image_0] = "http://pic.nnxy.cn/upfile/default/photo/original/14331469964373.JPG"
                        it[school_image_1] = "http://pic.nnxy.cn/upfile/default/photo/original/14331470023364.jpg"
                        it[school_image_2] = "http://pic.nnxy.cn/upfile/default/photo/original/14331470290851.jpg"
                        it[school_image_3] = "http://pic.nnxy.cn/upfile/default/photo/original/14331470316913.jpg"
                        it[school_image_4] = "http://pic.nnxy.cn/upfile/default/photo/original/14333947436115.jpg"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            is SchoolGuideTimeTable -> {

            }

            is SchoolDormitoryTable -> {

            }
        }
    }

    override suspend fun update(table: Table, where: Any?, model: Any?) {
        when (table) {
            is UsersTable -> {

            }
            is SchoolInfoTable -> {

            }

            is SchoolGuideTimeTable -> {

            }

            is SchoolDormitoryTable -> {

            }
        }
    }

    override suspend fun insert(table: Table, model: Any?): isSuccess {
        var insertCode = isSuccess(false)
        when (table) {
            is UsersTable -> {
                dbQuery {
                    try {
                        val users = model as Users
                        val avatar = users.user_avatar ?: ""
                        val school = users.user_school ?: ""
                        val college = users.user_college ?: ""
                        val nameReal = users.user_name ?: ""
                        val idCard = users.user_id_card ?: ""
                        val dormitory = users.user_dormitory ?: ""
                        table.insert {
                            it[phone_number] = users.phone_number
                            it[password] = users.password
                            it[name] = users.name
                            it[user_avatar] = avatar
                            it[user_school] = school
                            it[user_college] = college
                            it[user_name] = nameReal
                            it[user_id_card] = idCard
                            it[user_dormitory] = dormitory
                        }
                        insertCode = isSuccess(true)
                    } catch (e: Exception) {
                        insertCode = isSuccess(isSuccess = false, errorReason = e.toString())
                        e.printStackTrace()
                    }
                }
            }

            is SchoolInfoTable -> {

            }

            is SchoolGuideTimeTable -> {

            }

            is SchoolDormitoryTable -> {

            }
        }
        return insertCode
    }

    override suspend fun select(table: Table, where: Any): Any {
        val contentList = selectAll(table)
        var returnContent: Any? = null

        when (table) {
            is UsersTable -> {
                contentList?.let {
                    it.forEach { user ->
                        returnContent = if (where == (user as Users).id)
                            user
                        else
                            isSuccess(false, errorReason = "select_info_error")

                    }
                }
            }
            is SchoolInfoTable -> {
                contentList?.let {
                    it.forEach { schoolInfo ->
                        returnContent = if ((schoolInfo as SchoolInfo).school_id == where)
                            schoolInfo
                        else
                            isSuccess(false, errorReason = "select_info_error")

                    }
                }
            }

            is SchoolGuideTimeTable -> {
                contentList?.let {
                    it.forEach { guideTime ->
                        returnContent = if (where == (guideTime as SchoolGuideTime).school_id)
                            guideTime
                        else
                            isSuccess(false, errorReason = "select_info_error")

                    }
                }
            }

            is SchoolDormitoryTable -> {
                contentList?.let {
                    it.forEach { dormitory ->
                        returnContent = if (where == (dormitory as SchoolInfo).school_id)
                            dormitory
                        else
                            isSuccess(false, errorReason = "select_info_error")

                    }
                }
            }
        }
        return returnContent ?: isSuccess(false, errorReason = "select_info_error")
    }

    override suspend fun selectAll(table: Table): List<Any>? {
        var list: List<Any>? = null
        when (table) {
            is UsersTable -> {
                list = dbQuery {
                    table.selectAll().map { row ->
                        selectUsers(row)
                    }
                }

            }
            is SchoolInfoTable -> {
                list = dbQuery {
                    table.selectAll().map { row ->
                        selectSchoolInfo(row)
                    }
                }
            }

            is SchoolGuideTimeTable -> {
                list = dbQuery {
                    table.selectAll().map { row ->
                        selectGuideTime(row)
                    }
                }

            }

            is SchoolDormitoryTable -> {
                list = dbQuery {
                    table.selectAll().map { row ->
                        selectSchoolDormitory(row)
                    }
                }
            }
        }

        return list
    }

    private fun selectSchoolDormitory(row: ResultRow): SchoolDormitory =
        com.shen.model.SchoolDormitory(
            school_id = row[SchoolDormitoryTable.school_id],
            dormitory_id = row[SchoolDormitoryTable.dormitory_id],
            dormitory_name = row[SchoolDormitoryTable.dormitory_name],
            dormitory_student_list = listOf(
                row[SchoolDormitoryTable.dormitory_student_0],
                row[SchoolDormitoryTable.dormitory_student_1],
                row[SchoolDormitoryTable.dormitory_student_2],
                row[SchoolDormitoryTable.dormitory_student_3]
            )
        )

    private fun selectGuideTime(row: ResultRow): SchoolGuideTime =
        com.shen.model.SchoolGuideTime(
            school_id = row[SchoolGuideTimeTable.school_id],
            guide_college = row[SchoolGuideTimeTable.guide_college],
            guide_time_one = row[SchoolGuideTimeTable.guide_time_one],
            guide_time_two = row[SchoolGuideTimeTable.guide_time_two]
        )

    private fun selectSchoolInfo(row: ResultRow): SchoolInfo =
        SchoolInfo(
            school_id = row[SchoolInfoTable.school_id],
            school_name = row[SchoolInfoTable.school_name],
            school_address = row[SchoolInfoTable.school_address],
            school_introduce = row[SchoolInfoTable.school_introduce],
            image_show_list = listOf(
                row[SchoolInfoTable.school_image_0],
                row[SchoolInfoTable.school_image_1],
                row[SchoolInfoTable.school_image_2],
                row[SchoolInfoTable.school_image_3],
                row[SchoolInfoTable.school_image_4]
            )
        )

    private fun selectUsers(row: ResultRow): Users =
        Users(
            id = row[UsersTable.id],
            name = row[UsersTable.name],
            phone_number = row[UsersTable.phone_number],
            password = row[UsersTable.password],
            user_avatar = row[UsersTable.user_avatar],
            user_school = row[UsersTable.user_school],
            user_college = row[UsersTable.user_college],
            user_name = row[UsersTable.user_name],
            user_id_card = row[UsersTable.user_id_card],
            user_dormitory = row[UsersTable.user_dormitory]
        )


    private fun dbConnect(): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "org.h2.Driver"
        hikariConfig.jdbcUrl = "jdbc:h2:~/main"
        hikariConfig.username = "shen"
        hikariConfig.password = "1234"
        hikariConfig.maximumPoolSize = 20
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}