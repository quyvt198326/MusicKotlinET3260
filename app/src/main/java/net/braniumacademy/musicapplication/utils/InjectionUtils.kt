package net.braniumacademy.musicapplication.utils

import android.content.Context
import net.braniumacademy.musicapplication.data.repository.recent.RecentSongRepositoryImpl
import net.braniumacademy.musicapplication.data.repository.song.SongRepositoryImpl
import net.braniumacademy.musicapplication.data.source.SongDataSource
import net.braniumacademy.musicapplication.data.source.local.AppDatabase
import net.braniumacademy.musicapplication.data.source.local.recent.LocalRecentSongDataSource
import net.braniumacademy.musicapplication.data.source.local.song.LocalSongDataSource

object InjectionUtils {
    fun provideRecentSongDataSource(
        context: Context
    ): LocalRecentSongDataSource {
        val database = AppDatabase.getInstance(context)
        return LocalRecentSongDataSource(database.recentSongDao())
    }

    fun provideRecentSongRepository(
        dataSource: LocalRecentSongDataSource
    ): RecentSongRepositoryImpl {
        return RecentSongRepositoryImpl(dataSource)
    }

    fun provideSongDataSource(context: Context): SongDataSource.Local {
        val database = AppDatabase.getInstance(context)
        return LocalSongDataSource(database.songDao())
    }

    fun provideSongRepository(
        dataSource: SongDataSource.Local
    ): SongRepositoryImpl {
        return SongRepositoryImpl(dataSource)
    }
}