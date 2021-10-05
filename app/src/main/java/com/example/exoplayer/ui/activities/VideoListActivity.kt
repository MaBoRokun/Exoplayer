package com.example.exoplayer.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exoplayer.ui.adapter.VideoRecycleAdapter
import com.example.exoplayer.databinding.VideosListBinding
import com.example.exoplayer.model.Video
import com.example.exoplayer.network.RetrofitService
import com.example.exoplayer.repository.VideoRepository
import com.example.exoplayer.viewmodel.RoomViewModel
import com.example.exoplayer.viewmodel.VideoViewModel
import com.example.exoplayer.viewmodel.VideoViewModelFactory


class VideoListActivity : AppCompatActivity(), VideoRecycleAdapter.OnItemClickListener {

    private lateinit var binding: VideosListBinding
    lateinit var videoViewModel: VideoViewModel
    lateinit var roomViewModel: RoomViewModel
    private lateinit var videoAdapter: VideoRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideosListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        initViewModels()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@VideoListActivity)
            videoAdapter = VideoRecycleAdapter(this@VideoListActivity)
            binding.recyclerView.adapter = videoAdapter
        }
    }

    private fun initViewModels() {
        roomViewModel = ViewModelProvider(
            this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(this.application)
        )
            .get(RoomViewModel::class.java)

        roomViewModel.getRecordsObserver().observe(this, object : Observer<List<Video>> {
            override fun onChanged(t: List<Video>?) {
                t?.forEach {
                }
            }
        })

        videoViewModel =
            ViewModelProvider(this, VideoViewModelFactory(VideoRepository(RetrofitService)))
                .get(
                    VideoViewModel::class.java
                )
        videoViewModel.videoList.observe(this, { data ->
            videoAdapter.submitList(data)
            var index: Long = 0
            data.forEach { video ->
                video.id = index
                roomViewModel.insertRecord(video)
                index++
            }
        })

        videoViewModel.getVideos()
    }

    override fun onItemClick(position: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}