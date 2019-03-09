package util

import com.shen.model.isSuccess
import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.Table

interface DatabaseHelper {
    fun initContent(@NotNull table: Table)
    suspend fun update(@NotNull table: Table, where: Any? = null, model: Any? = null)
    suspend fun insert(@NotNull table: Table, model: Any? = null): isSuccess
    suspend fun select(@NotNull table: Table, where: Any): Any
    suspend fun selectAll(@NotNull table: Table): List<Any>?
}