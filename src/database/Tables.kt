package com.shen.database

import org.jetbrains.exposed.sql.Table

open class UsersInfo : Table() {
    val id = integer("id").primaryKey().autoIncrement().nullable()
    //昵称(显示在外的)
    val name = varchar("name", 255)
    //手机号
    val phone_number = integer("phone_number").uniqueIndex()
    //密码
    val password = varchar("password", 255)
    //用户头像
    val user_avatar = varchar("user_avatar", 255)
    //用户学校
    val user_school = varchar("user_school", 255)
    //用户学院
    val user_college = varchar("user_college", 255)
    //用户姓名
    val user_name = varchar("user_name", 255)
    //用户身份证号
    val user_id_card = varchar("user_id_card", 255)
}

open class SchoolInfo : Table() {
    val school_id = integer("school_id").primaryKey().autoIncrement()
    val school_name = varchar("school_name", 255)
    val school_address = varchar("school_address", 255)
    val school_introduce = varchar("school_introduce", 255)

    val school_image_0 = varchar("school_image_0", 255)
    val school_image_1 = varchar("school_image_1", 255)
    val school_image_2 = varchar("school_image_2", 255)
    val school_image_3 = varchar("school_image_3", 255)
    val school_image_4 = varchar("school_image_4", 255)
}

open class SchoolGuideTime : Table() {
    val guide_college = (varchar("guide_college", 255) references UsersTable.user_college).primaryKey().nullable()
    val guide_time_one = varchar("guide_time_one", 255)
    val guide_time_two = varchar("guide_time_two", 255)
}

object UsersTable : UsersInfo()
object SchoolInfoTable : SchoolInfo()
object SchoolGuideTimeTable : SchoolGuideTime()