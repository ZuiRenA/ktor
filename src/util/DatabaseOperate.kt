package com.shen.util

import com.shen.database.SchoolDormitoryTable
import com.shen.database.SchoolGuideTimeTable
import com.shen.database.SchoolInfoTable
import com.shen.database.UsersTable
import com.shen.model.isSuccess
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteIgnoreWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

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
                }

                else -> { content = null }
            }
        }

        return content ?: isSuccess(false)
    }


}