package com.example.exoplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.exoplayer.database.VideoDao
import com.example.exoplayer.di.MyApp
import com.example.exoplayer.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var videoDao: VideoDao

    var DBLocalData: MutableLiveData<List<Video>>

    init {
        (application as MyApp).getAppComponent().inject(this)
        DBLocalData = MutableLiveData()
        getAllRecords()
    }

    fun getRecordsObserver(): MutableLiveData<List<Video>> {
        return DBLocalData
    }

    fun getAllRecords() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = videoDao.getAllFromDB()
                DBLocalData.postValue(list)
            }
        }
    }

    fun insertRecord(video: Video) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                videoDao.insert(video)
                getAllRecords()
            }
        }
    }
}