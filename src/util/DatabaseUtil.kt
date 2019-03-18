package com.shen.util

import com.shen.database.DatabaseFactory
import com.shen.database.UsersTable
import com.shen.model.Upload
import com.shen.model.Users
import com.shen.model.isSuccess

object DatabaseUtil {
    suspend fun uploadAvatar(upload: Upload): isSuccess {
        val content = DatabaseFactory.select(UsersTable, upload.user_number)
        return if (content is Users) {
            val uploadUsers = Users(
                id = content.id,
                name = content.name,
                phone_number = content.phone_number,
                password = content.password,
                user_avatar = upload.image_address,
                user_school = content.user_school,
                user_college = content.user_college,
                user_name = content.user_name,
                user_id_card = content.user_id_card,
                user_dormitory = content.user_dormitory
            )
            DatabaseFactory.update(UsersTable, upload.user_number, uploadUsers)
        } else {
            isSuccess(false, "phone_number can't find")
        }
    }
}