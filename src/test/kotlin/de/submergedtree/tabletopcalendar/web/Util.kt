package de.submergedtree.tabletopcalendar.web

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Util {

    @Test
    fun `parse comma separated string into array`() {
        val toTest = "blub,blab,bleb"
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals("blub", res[0])
        Assertions.assertEquals("blab", res[1])
        Assertions.assertEquals("bleb", res[2])
    }

    @Test
    fun `parse one into array`() {
        val toTest = "blub"
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals(1, res.size)
        Assertions.assertEquals("blub", res[0])
    }

    @Test
    fun `parse one with additional ,`() {
        val toTest = "blub,"
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals(1, res.size)
        Assertions.assertEquals("blub", res[0])
    }

    @Test
    fun `parse single ,`() {
        val toTest = ","
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals(0, res.size)
    }

    @Test
    fun `try to parse empty`() {
        val toTest = ""
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals(res, ArrayList<String>())
    }

    @Test
    fun `try to parse blanc`() {
        val toTest = "  "
        val res = parseCommaSeparatedStringToArray(toTest)
        Assertions.assertEquals(res, ArrayList<String>())
    }
}