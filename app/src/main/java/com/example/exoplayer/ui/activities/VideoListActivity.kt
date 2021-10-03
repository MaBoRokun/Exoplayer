package com.example.exoplayer.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.exoplayer.ui.adapter.VideoRecycleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.exoplayer.DAO.VideoDatabase
import com.example.exoplayer.databinding.VideosListBinding
import com.example.exoplayer.model.Video
import com.example.exoplayer.network.RetrofitService
import com.example.exoplayer.repository.VideoRepository
import com.example.exoplayer.viewmodel.VideoViewModel
import com.example.exoplayer.viewmodel.VideoViewModelFactory
import kotlinx.coroutines.Dispatchers


class VideoListActivity : AppCompatActivity(), VideoRecycleAdapter.OnItemClickListener {

    private lateinit var binding: VideosListBinding
    lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: VideoRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideosListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        videoViewModel = ViewModelProvider(this, VideoViewModelFactory(VideoRepository(RetrofitService)))
            .get(
                VideoViewModel::class.java
            )
        videoViewModel.videoList.observe(this, Observer {
            videoAdapter.submitList(it)
            var index:Long = 0
            it.forEach {
                it.id=index
                toLocalStorage(it)
                index++
            }
        })

        videoViewModel.getVideos()
    }

    private fun toLocalStorage(video: Video) {
        CoroutineScope(Dispatchers.Default).launch {
            val db = Room.databaseBuilder(
                applicationContext,
                VideoDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration()
                .build()
            val VideosDAO = db.videoDao()
            VideosDAO.insert(video)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@VideoListActivity)
            videoAdapter = VideoRecycleAdapter(this@VideoListActivity)
            binding.recyclerView.adapter = videoAdapter
        }
    }

    override fun onItemClick(position: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
//        CoroutineScope(IO).launch {
//            Room.databaseBuilder(
//                applicationContext,
//                VideoDatabase::class.java, "database-name"
//            ).fallbackToDestructiveMigration()
//                .build().clearAllTables()
//        }

    }
}