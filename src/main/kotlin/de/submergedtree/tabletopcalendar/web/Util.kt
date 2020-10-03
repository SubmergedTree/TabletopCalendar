package de.submergedtree.tabletopcalendar.web

fun parseCommaSeparatedStringToArray(str: String): List<String> {
    val trimmed = str.trimEnd(',')
    if (trimmed.isBlank()) {
        return ArrayList()
    }
    return trimmed.split(",")
}