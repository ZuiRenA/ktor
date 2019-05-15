package com.shen.model

import org.h2.engine.User

object InitUserInfo {
    val usersList = listOf(
        Users(
            id = 1,
            name = "昵称1",
            phone_number = 13405665741,
            password = "aaaa",
            user_avatar = "http://pic34.nipic.com/20131104/13264764_101028322111_2.jpg",
            user_school = "南宁学院",
            user_college = "高博软件学院",
            user_name = "沈超1",
            user_id_card = "320583199707261612",
            user_dormitory = "103",
            user_letter = "yes",
            user_permission = "true"
        ),
        Users(
            id = 2,
            name = "昵称2",
            phone_number = 13405665742,
            password = "aaaa",
            user_avatar = "http://pic34.nipic.com/20131104/13264764_101028322111_2.jpg",
            user_school = "南宁学院",
            user_college = "高博软件学院",
            user_name = "沈超2",
            user_id_card = "320583199707261612",
            user_dormitory = "103",
            user_letter = "yes",
            user_permission = "true"
        )
    )
}