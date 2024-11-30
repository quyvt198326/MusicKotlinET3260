package net.braniumacademy.musicapplication.data.source

import kotlinx.coroutines.flow.Flow
import net.braniumacademy.musicapplication.data.model.song.Song

interface SongDataSource {
    interface Local {
        val songs: List<Song>

        val favoriteSongs: Flow<List<Song>>

        suspend fun insert(vararg songs: Song)

        suspend fun delete(song: Song)

        suspend fun update(song: Song)

        suspend fun updateFavorite(id: String, favorite: Boolean)
    }

    interface Remote {
        // todo
    }
}