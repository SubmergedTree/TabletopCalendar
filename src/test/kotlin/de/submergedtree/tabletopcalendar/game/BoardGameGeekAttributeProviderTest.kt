package de.submergedtree.tabletopcalendar.game

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import de.submergedtree.tabletopcalendar.game.impl.BoardGameGeekGameAttributeProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@SpringBootTest
class BoardGameGeekAttributeProviderTest @Autowired constructor(val bggAp: BoardGameGeekGameAttributeProvider) {

    val testStr = "<boardgames termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
            "    <boardgame objectid=\"15\">\n" +
            "        <name primary=\"true\">Cosmic Encounter</name>\n" +
            "        <yearpublished>1977</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"39463\">\n" +
            "        <name primary=\"true\">Cosmic Encounter</name>\n" +
            "        <yearpublished>2008</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"40529\">\n" +
            "        <name primary=\"true\">Cosmic Encounter</name>\n" +
            "        <yearpublished>1991</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"40531\">\n" +
            "        <name primary=\"true\">Cosmic Encounter</name>\n" +
            "        <yearpublished>2000</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"298572\">\n" +
            "        <name primary=\"true\">Cosmic Encounter Duel</name>\n" +
            "        <yearpublished>2020</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"313010\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: 42nd Anniversary Edition</name>\n" +
            "        <yearpublished>2018</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"288318\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Booster Promo Alien</name>\n" +
            "        <yearpublished>2019</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"114276\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Alliance</name>\n" +
            "        <yearpublished>2012</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"87507\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Conflict</name>\n" +
            "        <yearpublished>2011</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"153971\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Dominion</name>\n" +
            "        <yearpublished>2014</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"206366\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Eons</name>\n" +
            "        <yearpublished>2016</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"61001\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Incursion</name>\n" +
            "        <yearpublished>2010</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"143760\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Cosmic Storm</name>\n" +
            "        <yearpublished>2013</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4715\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #1</name>\n" +
            "        <yearpublished>1977</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4716\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #2</name>\n" +
            "        <yearpublished>1977</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4717\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #3</name>\n" +
            "        <yearpublished>1978</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4718\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #4</name>\n" +
            "        <yearpublished>1979</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4719\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #5</name>\n" +
            "        <yearpublished>1980</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4720\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #6</name>\n" +
            "        <yearpublished>1981</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4721\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #7</name>\n" +
            "        <yearpublished>1981</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4722\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #8</name>\n" +
            "        <yearpublished>1982</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"4723\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: Expansion Set #9</name>\n" +
            "        <yearpublished>1983</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"168391\">\n" +
            "        <name primary=\"true\">Cosmic Encounter: White Dwarf Magazine Expansion</name>\n" +
            "        <yearpublished>1986</yearpublished>\n" +
            "    </boardgame>\n" +
            "    <boardgame objectid=\"2739\">\n" +
            "        <name primary=\"true\">More Cosmic Encounter</name>\n" +
            "        <yearpublished>1992</yearpublished>\n" +
            "    </boardgame>\n" +
            "</boardgames>"

    val testStr2 = "<boardgames termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
            "    <boardgame objectid=\"15\">\n" +
            "        <name primary=\"true\">Cosmic Encounter</name>\n" +
            "        <yearpublished>1977</yearpublished>\n" +
            "    </boardgame>\n" +
            "</boardgames>"

    @Test
    fun foo() {
        bggAp.search("Cosmic%20Encounter");

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BGGBoardgameSearchResponse(
                                    var objectid: String = "",
                                          var name: String = "",
                                           var yearpublished: String = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BGGSearchResponse(var boardgame: BGGBoardgameSearchResponse = BGGBoardgameSearchResponse(),
            var termsofuse: String = "")


    @Test
    fun `test Serialization`() {

        print(convertXmlString2DataObject(testStr, Array<BGGBoardgameSearchResponse>::class.java))
    }

    fun convertXmlString2DataObject(xmlString: String, cls: Class<*>): Any {
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(xmlString, cls)
    }


}