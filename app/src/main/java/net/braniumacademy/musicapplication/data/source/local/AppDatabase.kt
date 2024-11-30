package net.braniumacademy.musicapplication.data.source.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.braniumacademy.musicapplication.data.model.RecentSong
import net.braniumacademy.musicapplication.data.model.album.Album
import net.braniumacademy.musicapplication.data.model.playlist.Playlist
import net.braniumacademy.musicapplication.data.model.song.Song
import net.braniumacademy.musicapplication.data.source.local.recent.RecentSongDao
import net.braniumacademy.musicapplication.data.source.local.song.SongDao

@Database(
    entities = [
        Song::class,
        Playlist::class,
        Album::class,
        RecentSong::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun albumDao(): AlbumDao
    abstract fun recentSongDao(): RecentSongDao

    companion object {
        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (_instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (_instance == null) {
                        _instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "music.db"
                        ).build()
                    }
                }
            }
            return _instance!!
        }
    }
}