package util

import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.Table

interface DatabaseHelper {
    abstract fun update(@NotNull table: Table)
}