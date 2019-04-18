package com.shen.model

import java.io.File

data class PandP(
    val phone_number: Long,
    val password: String
)

data class isSuccess(
    val isSuccess: Boolean,
    val respond: Any? = null,
    val errorReason: String? = ""
)

data class Id(
    val id: Int
)

data class Upload(
    val user_number: Long,
    val image_address: String
)

data class Content(
    val text: String
)

data class Users(
    val id: Int?,
    val name: String,
    val phone_number: Long,
    val password: String,
    val user_avatar: String? = null,
    val user_school: String? = null,
    val user_college: String? = null,
    val user_name: String? = null,
    val user_id_card: String? = null,
    val user_dormitory: String? = null
)

data class SchoolInfo(
    val school_id: Int,
    val school_name: String,
    val school_address: String,
    val school_introduce: String,
    val image_show_list: List<String>
)

data class SchoolGuideTime(
    val school_id: Int?,
    val guide_college: String,
    val guide_time_one: String,
    val guide_time_two: String
)

data class SchoolDormitory(
    val school_id: Int?,
    val dormitory_id: Int,
    val dormitory_name: String,
    val dormitory_student_list: List<String>
)

data class School(
    val school_id: Int,
    val school_name: String
)