package net.braniumacademy.musicapplication.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import net.braniumacademy.musicapplication.data.model.song.Song
import net.braniumacademy.musicapplication.ui.dialog.SongOptionMenuDialogFragment
import net.braniumacademy.musicapplication.ui.dialog.SongOptionMenuViewModel
import net.braniumacademy.musicapplication.ui.viewmodel.SharedViewModel

open class PlayerBaseFragment : Fragment() {
    protected fun playSong(song: Song, index: Int, playlistName: String) {
        val sharedViewModel = SharedViewModel.instance
        sharedViewModel.setCurrentPlaylist(playlistName)
        sharedViewModel.setIndexToPlay(index)
    }

    protected fun showOptionMenu(song: Song) {
        val menuDialogFragment = SongOptionMenuDialogFragment.newInstance
        val menuDialogViewModel: SongOptionMenuViewModel by activityViewModels()
        menuDialogViewModel.setSong(song)
        menuDialogFragment.show(
            requireActivity().supportFragmentManager,
            SongOptionMenuDialogFragment.TAG
        )
    }
}