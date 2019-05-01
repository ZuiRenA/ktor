package com.shen.util

import com.shen.database.*
import com.shen.model.*
import com.shen.model.SchoolDormitory
import com.shen.model.SchoolGuideTime
import com.shen.model.SchoolInfo
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
                user_dormitory = baseUser.user_dormitory,
                user_letter = baseUser.user_letter
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

    suspend fun uploadCALetter(letter: Letter): isSuccess {
        val content = DatabaseFactory.select(UsersTable, letter.phone_number)
        return if ((content as isSuccess).isSuccess) {
            val baseUser = content.respond as Users
            val uploadUsers = Users(
                id = baseUser.id,
                name = baseUser.name,
                phone_number = baseUser.phone_number,
                password = baseUser.password,
                user_avatar = baseUser.user_avatar,
                user_school = baseUser.user_school ?: baseUser.user_school,
                user_college = baseUser.user_college ?: baseUser.user_college,
                user_name = baseUser.user_name ?: baseUser.user_name,
                user_id_card = baseUser.user_id_card ?: baseUser.user_id_card,
                user_dormitory = baseUser.user_dormitory,
                user_letter = letter.uri
            )
            DatabaseFactory.update(UsersTable, letter.phone_number, uploadUsers)
        } else {
            isSuccess(false, "phone_number can not find")
        }
    }

    suspend fun selectGuideTime(schoolId: Int): isSuccess {
        val guideTimeList = DatabaseFactory.selectAll(SchoolGuideTimeTable) as List<SchoolGuideTime>
        val collegeList = mutableListOf<SchoolGuideTime>()
        guideTimeList.forEach {
            if (it.school_id == schoolId)
                collegeList.add(it)
        }
        return if (collegeList.isEmpty())
            isSuccess(false, errorReason = "查询不到院系信息")
        else
            isSuccess(true, collegeList)
    }

    suspend fun changePassword(password: Password): isSuccess {
        val content = DatabaseFactory.select(UsersTable, password.phone_number)
        return if ((content as isSuccess).isSuccess) {
            val baseUser = content.respond as Users
            val uploadUsers = Users(
                id = baseUser.id,
                name = baseUser.name,
                phone_number = baseUser.phone_number,
                password = password.password,
                user_avatar = baseUser.user_avatar,
                user_school = baseUser.user_school ?: baseUser.user_school,
                user_college = baseUser.user_college ?: baseUser.user_college,
                user_name = baseUser.user_name ?: baseUser.user_name,
                user_id_card = baseUser.user_id_card ?: baseUser.user_id_card,
                user_dormitory = baseUser.user_dormitory,
                user_letter = baseUser.user_letter
            )
            DatabaseFactory.update(UsersTable, password.phone_number, uploadUsers)
        } else {
            isSuccess(false, "phone_number can not find")
        }
    }

    suspend fun selectDormitory(schoolId: Int?): isSuccess {
        val guideTimeList = DatabaseFactory.selectAll(SchoolDormitoryTable) as List<SchoolDormitory>
        val collegeList = mutableListOf<SchoolDormitory>()
        guideTimeList.forEach {
            if (it.school_id == schoolId)
                collegeList.add(it)
        }
        return if (collegeList.isEmpty())
            isSuccess(false, errorReason = "查询不到学校信息")
        else
            isSuccess(true, collegeList)
    }

    suspend fun studentSelectDor(selectDor: SelectDor):isSuccess {
        val userInfo = (DatabaseFactory.select(UsersTable, selectDor.phone_number) as isSuccess).respond as Users
        val temp = SelectOption(userInfo.name, selectDor.id, selectDor.index)
        val result = DatabaseFactory.update(SchoolDormitoryTable, temp.id, temp)
        if (result.isSuccess) {
            val name = DatabaseFactory.select(SchoolDormitoryTable, temp.id) as isSuccess
            val uploadUsers = Users(
                id = userInfo.id,
                name = userInfo.name,
                phone_number = userInfo.phone_number,
                password = userInfo.password,
                user_avatar = userInfo.user_avatar,
                user_school = userInfo.user_school,
                user_college = userInfo.user_college,
                user_name = userInfo.user_name,
                user_id_card = userInfo.user_id_card,
                user_dormitory = (name.respond as SchoolDormitory).dormitory_name,
                user_letter = userInfo.user_letter
            )

            return DatabaseFactory.update(UsersTable, selectDor.phone_number, uploadUsers)
        }

        return isSuccess(false, "选择宿舍失败")
    }
}