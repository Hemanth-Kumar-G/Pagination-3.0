package com.hemanthdDev.paging.ui

import androidx.lifecycle.ViewModel
import com.hemanthdDev.paging.data.db.RepoDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repoDatabase: RepoDatabase) : ViewModel() {
}