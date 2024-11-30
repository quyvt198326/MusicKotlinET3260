package net.braniumacademy.musicapplication.ui.playing

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import net.braniumacademy.musicapplication.R
import net.braniumacademy.musicapplication.data.model.song.Song
import net.braniumacademy.musicapplication.databinding.FragmentMiniPlayerBinding
import net.braniumacademy.musicapplication.ui.viewmodel.MediaPlayerViewModel
import net.braniumacademy.musicapplication.ui.viewmodel.SharedViewModel

class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMiniPlayerBinding
    private val viewModel: MiniPlayerViewModel by activityViewModels()
    private var mediaController: MediaController? = null
    private lateinit var pressedAnimator: Animator
    private lateinit var rotationAnimator: ObjectAnimator
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
        setupAnimator()
        setupMediaController()
        setupObserve()
    }

    override fun onClick(v: View) {
        pressedAnimator.setTarget(v)
        pressedAnimator.start()
        when (v) {
            binding.btnMiniPlayerPlayPause -> mediaController?.let {
                if (it.isPlaying) {
                    it.pause()
                } else {
                    it.play()
                }
            }

            binding.btnMiniPlayerSkipNext ->
                mediaController?.let {
                    if (it.hasNextMediaItem()) {
                        it.seekToNextMediaItem()
                        rotationAnimator.end()
                    }
                }

            binding.btnMiniPlayerFavorite -> setupFavorite()
        }
    }

    private fun setupFavorite() {
        val playingSong = sharedViewModel.playingSong.value
        playingSong?.let {
            val song = it.song
            song!!.favorite = !song.favorite
            updateFavoriteStatus(song)
            sharedViewModel.updateFavoriteStatus(song)
        }
    }

    private fun setupView() {
        binding.btnMiniPlayerFavorite.setOnClickListener(this)
        binding.btnMiniPlayerPlayPause.setOnClickListener(this)
        binding.btnMiniPlayerSkipNext.setOnClickListener(this)
        binding.root.setOnClickListener {
            navigateToNowPlaying()
        }
    }
    
    private fun setupViewModel() {
        sharedViewModel = SharedViewModel.instance
    }

    private fun setupAnimator() {
        pressedAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        rotationAnimator = ObjectAnimator
            .ofFloat(binding.imageMiniPlayerArtwork, "rotation", 0f, 360f)
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.duration = 12000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.repeatMode = ObjectAnimator.RESTART
    }

    private fun setupMediaController() {
        MediaPlayerViewModel.instance.mediaController.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                mediaController = it
                setupListener()
                setupObserveForMediaController()
            }
        }
    }

    private fun setupListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                viewModel.setPlayingState(isPlaying)
            }
        })
    }

    private fun setupObserveForMediaController() {
        mediaController?.let {
            viewModel.mediaItems.observe(viewLifecycleOwner) { mediaItems ->
                it.setMediaItems(mediaItems)
            }
            sharedViewModel.indexToPlay.observe(viewLifecycleOwner) { index ->
                if (index > -1 && it.mediaItemCount > index) {
                    it.seekTo(index, 0)
                    it.prepare()
//                    it.play()
                }
            }
        }
    }

    private fun setupObserve() {
        sharedViewModel.playingSong.observe(viewLifecycleOwner) {
            it.song?.let { song ->
                showSongInfo(song)
            }
        }
        sharedViewModel.currentPlaylist.observe(viewLifecycleOwner) {
            viewModel.setMediaItem(it.mediaItems)
        }

        viewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) { // playing
                binding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_pause_circle)
                if (rotationAnimator.isPaused) {
                    rotationAnimator.resume()
                } else if (!rotationAnimator.isRunning) {
                    rotationAnimator.start()
                }
            } else {
                binding.btnMiniPlayerPlayPause.setImageResource(R.drawable.ic_play_circle)
                rotationAnimator.pause()
            }
        }
    }

    private fun showSongInfo(song: Song) {
        binding.textMiniPlayerTitle.text = song.title
        binding.textMiniPlayerArtist.text = song.artist
        Glide.with(requireContext())
            .load(song.image)
            .error(R.drawable.ic_album)
            .circleCrop()
            .into(binding.imageMiniPlayerArtwork)
        updateFavoriteStatus(song)
    }

    private fun updateFavoriteStatus(song: Song) {
        val favoriteIcon = if (song.favorite) {
            R.drawable.ic_favorite_on
        } else {
            R.drawable.ic_favorite_off
        }
        binding.btnMiniPlayerFavorite.setImageResource(favoriteIcon)
    }

    private fun navigateToNowPlaying() {
        Intent(requireContext(), NowPlayingActivity::class.java).apply {
            startActivity(this)
        }
    }
}