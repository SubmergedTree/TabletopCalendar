package de.submergedtree.tabletopcalendar.web

import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

data class ErrorBody(val error: String)

fun parseCommaSeparatedStringToArray(str: String): List<String> {
    val trimmed = str.trimEnd(',')
    if (trimmed.isBlank()) {
        return ArrayList()
    }
    return trimmed.split(",")
}

fun <T>encodeObjectToJson(obj: T): String =
    Gson().toJson(obj)


fun encodeStringBase64(toEncode: String): String =
    Base64.getEncoder().encodeToString(toEncode.toByteArray())