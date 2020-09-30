package de.submergedtree.tabletopcalendar.game.impl

import de.submergedtree.tabletopcalendar.game.GameAttributeProvider
import de.submergedtree.tabletopcalendar.game.GameService
import de.submergedtree.tabletopcalendar.game.SearchObjectCacheService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class GameServiceImpl(@Qualifier("Discoverable") private val gameAttributeProviders: List<GameAttributeProvider>,
                      private val searchObjectCacheService: SearchObjectCacheService) : GameService {

    override fun searchGame(name: String) {
        TODO("Not yet implemented")
        // min string length > 3 and client side throttling. Wait 2 sec before calling api
        // query gameAttributeProviders
        // save in in memory db
        // (in memory db should be cleared each 5 min)
    }

    override fun getGame(gameId: String) {
        TODO("Not yet implemented")
        // lookup cache
        // search provider
        // load attributes and return Detail Game Object.
    }
}