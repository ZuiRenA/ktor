package com.shen.util

import com.shen.database.DatabaseFactory
import com.shen.database.SchoolGuideTimeTable
import com.shen.database.SchoolInfoTable
import com.shen.database.UsersTable
import com.shen.model.*
import java.lang.Exception

object DatabaseUtil {
    suspend fun uploadPersonData(users: Users): isSuccess {
        val content = DatabaseFactory.select(UsersTable, users.phone_number)
        return if ((content as isSuccess).isSuccess) {
            val baseUser = content.respond as Users
            val uploadUsers = Users(
                id = baseUser.id,
                name = users.name,
                phone_number = baseUser.phone_number,
                password = baseUser.password,
                user_avatar = users.user_avatar,
                user_school = users.user_school ?: baseUser.user_school,
                user_college = users.user_college ?: baseUser.user_college,
                user_name = users.user_name ?: baseUser.user_name,
                user_id_card = users.user_id_card ?: baseUser.user_id_card,
                user_dormitory = baseUser.user_dormitory
            )
            DatabaseFactory.update(UsersTable, users.phone_number, uploadUsers)
        } else {
            isSuccess(false, "phone_number can not find")
        }
    }

    suspend fun selectAllSchool(): isSuccess {
        var returnContent: isSuccess? = null
        try {
            val list = DatabaseFactory.selectAll(SchoolInfoTable) as List<SchoolInfo>
            val schoolList = mutableListOf<School>()
            list.forEach {
                schoolList.add(School(it.school_id, it.school_name))
            }
            returnContent = isSuccess(true, schoolList)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return returnContent ?: isSuccess(false, "查找学校列表失败")
    }

    suspend fun selectAllCollege(schoolId: Int): isSuccess {
        val temp = DatabaseFactory.selectAll(SchoolGuideTimeTable) as List<SchoolGuideTime>
        val collegeList = mutableListOf<String>()
        temp.forEach {
            if (it.school_id == schoolId)
                collegeList.add(it.guide_college)
        }

        return if (collegeList.isEmpty())
            isSuccess(false, errorReason = "查询不到院系信息")
        else
            isSuccess(true, collegeList as List<String>)
    }
}