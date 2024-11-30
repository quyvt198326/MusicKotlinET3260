package net.braniumacademy.musicapplication.data.source.local.song

import kotlinx.coroutines.flow.Flow
import net.braniumacademy.musicapplication.data.model.song.Song
import net.braniumacademy.musicapplication.data.source.SongDataSource

class LocalSongDataSource(
    private val songDao: SongDao
) : SongDataSource.Local {
    override val songs: List<Song>
        get() = songDao.songs

    override val favoriteSongs: Flow<List<Song>>
        get() = songDao.favoriteSongs

    override suspend fun insert(vararg songs: Song) {
        songDao.insert(*songs)
    }

    override suspend fun delete(song: Song) {
        songDao.delete(song)
    }

    override suspend fun update(song: Song) {
        songDao.update(song)
    }

    override suspend fun updateFavorite(id: String, favorite: Boolean) {
        songDao.updateFavorite(id, favorite)
    }
}