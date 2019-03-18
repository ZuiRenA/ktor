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
                    InitSchoolInfo.schoolInfo.forEach { schoolInfo ->
                        table.insert {
                            it[school_id] = schoolInfo.school_id
                            it[school_name] = schoolInfo.school_name
                            it[school_address] = schoolInfo.school_address
                            it[school_introduce] = schoolInfo.school_introduce
                            val initList = listOf(
                                school_image_0, school_image_1,
                                school_image_2, school_image_3, school_image_4
                            )
                            schoolInfo.image_show_list.forEachIndexed { index, content ->
                                it[initList[index]] = content
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            is SchoolGuideTimeTable -> {
                try {
                    InitSchoolGuideTime.schoolGuideTime.forEach { schoolGuideTime ->
                        table.insert {
                            it[school_id] = schoolGuideTime.school_id
                            it[guide_college] = schoolGuideTime.guide_college
                            it[guide_time_one] = schoolGuideTime.guide_time_one
                            it[guide_time_two] = schoolGuideTime.guide_time_two
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            is SchoolDormitoryTable -> {
                try {
                    InitSchoolDormitory.schoolDormitory.forEach { schoolDormitory ->
                        table.insert {
                            it[school_id] = schoolDormitory.school_id
                            it[dormitory_id] = schoolDormitory.dormitory_id
                            it[dormitory_name] = schoolDormitory.dormitory_name
                            val initList = listOf(
                                dormitory_student_0, dormitory_student_1,
                                dormitory_student_2, dormitory_student_3
                            )
                            schoolDormitory.dormitory_student_list.forEachIndexed { index, student ->
                                it[initList[index]] = student
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override suspend fun update(table: Table, where: Any?, model: Any?): isSuccess {
        var returnContent = isSuccess(false)
        when (table) {
            is UsersTable -> {
                val users = model as Users
                transaction {
                    try {
                        table.update({ table.phone_number.eq(where as Long) }) {
                            it[table.name] = users.name
                            it[table.phone_number] = users.phone_number
                            it[table.password] = users.password
                            it[table.user_avatar] = users.user_avatar ?: ""
                            it[table.user_school] = users.user_school ?: ""
                            it[table.user_college] = users.user_college ?: ""
                            it[table.user_name] = users.user_name ?: ""
                            it[table.user_id_card] = users.user_id_card ?: ""
                            it[table.user_dormitory] = users.user_dormitory ?: ""
                        }
                        returnContent = isSuccess(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        returnContent = isSuccess(isSuccess = false, errorReason = "update error")
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
        return returnContent
    }

    override suspend fun insert(table: Table, model: Any?): isSuccess {
        var insertCode = isSuccess(false)
        when (table) {
            is UsersTable -> {
                dbSet {
                    try {
                        val users = model as Users
                        table.insert {
                            it[phone_number] = users.phone_number
                            it[password] = users.password
                            it[name] = users.name
                            it[user_avatar] = users.user_avatar ?: ""
                            it[user_school] = users.user_school ?: ""
                            it[user_college] = users.user_college ?: ""
                            it[user_name] = users.user_name ?: ""
                            it[user_id_card] = users.user_id_card ?: ""
                            it[user_dormitory] = users.user_dormitory ?: ""
                        }
                        insertCode = isSuccess(true)
                    } catch (e: Exception) {
                        insertCode = isSuccess(isSuccess = false, errorReason = e.toString())
                        e.printStackTrace()
                    }
                }
            }

            is SchoolInfoTable -> {
                transaction {
                    try {
                        val schoolInfo = model as SchoolInfo
                        table.insert {
                            it[school_name] = schoolInfo.school_name
                            it[school_address] = schoolInfo.school_address
                            it[school_introduce] = schoolInfo.school_introduce
                            val list = listOf(
                                school_image_0, school_image_1, school_image_2,
                                school_image_3, school_image_4
                            )
                            schoolInfo.image_show_list.forEachIndexed { index, content ->
                                it[list[index]] = content
                            }
                        }
                        insertCode = isSuccess(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        insertCode = isSuccess(isSuccess = false, errorReason = e.toString())
                    }
                }
            }

            is SchoolGuideTimeTable -> {
                transaction {
                    try {
                        val schoolGuideTime = model as SchoolGuideTime
                        table.insert {
                            it[school_id] = schoolGuideTime.school_id
                            it[guide_college] = schoolGuideTime.guide_college
                            it[guide_time_one] = schoolGuideTime.guide_time_one
                            it[guide_time_two] = schoolGuideTime.guide_time_two
                        }
                        insertCode = isSuccess(true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        insertCode = isSuccess(isSuccess = false, errorReason = e.toString())
                    }

                }
            }

            is SchoolDormitoryTable -> {
                transaction {
                    try {
                        val schoolDormitory = model as SchoolDormitory
                        table.insert {
                            it[school_id] = schoolDormitory.school_id
                            it[dormitory_id] = schoolDormitory.dormitory_id
                            it[dormitory_name] = schoolDormitory.dormitory_name
                            val list = listOf(
                                dormitory_student_0, dormitory_student_1,
                                dormitory_student_2, dormitory_student_3
                            )
                            schoolDormitory.dormitory_student_list.forEachIndexed { index, content ->
                                it[list[index]] = content
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        insertCode = isSuccess(isSuccess = false, errorReason = e.toString())
                    }
                }
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
                        if (where == (user as Users).phone_number) {
                            returnContent = user
                        }
                    }
                }
            }
            is SchoolInfoTable -> {
                contentList?.let {
                    it.forEach { schoolInfo ->
                        if ((schoolInfo as SchoolInfo).school_id == where) {
                            returnContent = schoolInfo
                        }
                    }
                }
            }

            is SchoolGuideTimeTable -> {
                contentList?.let {
                    it.forEach { guideTime ->
                        if (where == (guideTime as SchoolGuideTime).guide_college) {
                            returnContent = guideTime
                        }
                    }
                }
            }

            is SchoolDormitoryTable -> {
                contentList?.let {
                    it.forEach { dormitory ->
                        if (where == (dormitory as SchoolDormitory).dormitory_id) {
                            returnContent = dormitory
                        }
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
                list = dbSet {
                    table.selectAll().map { row ->
                        selectUsers(row)
                    }
                }

            }
            is SchoolInfoTable -> {
                list = dbSet {
                    table.selectAll().map { row ->
                        selectSchoolInfo(row)
                    }
                }
            }

            is SchoolGuideTimeTable -> {
                list = dbSet {
                    table.selectAll().map { row ->
                        selectGuideTime(row)
                    }
                }

            }

            is SchoolDormitoryTable -> {
                list = dbSet {
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
        hikariConfig.maximumPoolSize = 8
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbSet(
        block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}