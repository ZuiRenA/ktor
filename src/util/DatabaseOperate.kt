package com.shen.util

import com.shen.database.SchoolDormitoryTable
import com.shen.database.SchoolGuideTimeTable
import com.shen.database.SchoolInfoTable
import com.shen.database.UsersTable
import com.shen.model.SchoolDormitory
import com.shen.model.isSuccess
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteIgnoreWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.lang.Exception

object DatabaseOperate{

    fun deleteDatabaseSome(table: Table, where: Any): isSuccess {
        var content: isSuccess? = null
        transaction {
            when(table) {
                is UsersTable -> {
                    table.deleteWhere{ table.phone_number.eq(where as Long) }
                    content = isSuccess(true)
                }

                is SchoolInfoTable -> {
                    table.deleteWhere{ table.school_id.eq(where as Int) }
                    content = isSuccess(true)
                }

                is SchoolGuideTimeTable -> {
                    table.deleteWhere { table.guide_college.eq(where as String) }
                    content = isSuccess(true)
                }

                is SchoolDormitoryTable -> {
                    table.deleteWhere { table.dormitory_id.eq(where as Int) }
                    content = isSuccess(true)
                }

                else -> { content = null }
            }
        }

        return content ?: isSuccess(false)
    }

    fun updateSchoolDor(model: SchoolDormitory): isSuccess {
        var content: isSuccess? = null
        transaction {
            try {
                SchoolDormitoryTable.update({ SchoolDormitoryTable.dormitory_id.eq(model.dormitory_id) }) {
                    it[SchoolDormitoryTable.school_id] = model.school_id
                    it[SchoolDormitoryTable.dormitory_id] = model.dormitory_id
                    it[SchoolDormitoryTable.dormitory_name] = model.dormitory_name

                    val list = listOf(dormitory_student_0, dormitory_student_1,
                        dormitory_student_2, dormitory_student_3)
                    model.dormitory_student_list.forEachIndexed { index, s ->
                        it[list[index]] = s
                    }
                }
                content = isSuccess(true)
            } catch (e: Exception) {
                e.printStackTrace()
                content = isSuccess(false, errorReason = "$e")
            }
        }

        return content ?: isSuccess(false)
    }

}