package com.example.exoplayer.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exoplayer.Resource
import com.example.exoplayer.ui.adapter.VideoRecycleAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.exoplayer.model.Videos
import com.example.exoplayer.DAO.AppDatabase
import com.example.exoplayer.databinding.VideosListBinding


class VideoListActivity: AppCompatActivity(), VideoRecycleAdapter.OnItemClickListener {

    private lateinit var binding: VideosListBinding

    private lateinit var videoAdapter: VideoRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VideosListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRecyclerView()
        addDataSet()



    }

    private fun addDataSet(){
        CoroutineScope(IO).launch {
            val data = Resource.createDataSet()
            videoAdapter.submitList(data)

           val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration()
                .build()
            val VideosDAO = db.videoDAO()
            data.forEach {
                VideosDAO?.insert(it)
                Log.d("Room_Insert",it.toString())
            }
            val Videos: List<Videos>? = VideosDAO?.getAll()
            Log.d("Room_Select", Videos.toString())
        }
    }

    private fun initRecyclerView(){
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
        CoroutineScope(IO).launch {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration()
                .build().clearAllTables()
        }
    }
}