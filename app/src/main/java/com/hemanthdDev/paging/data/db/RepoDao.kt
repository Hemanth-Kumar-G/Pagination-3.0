package com.hemanthdDev.paging.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hemanthdDev.paging.data.model.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Repo>)

    @Query("select * from repos where name like :query or description like :query order by stars desc ,name asc")
    fun repoByName(query: String): PagingSource<Int, Repo>

    @Query("delete from repos")
    suspend fun clearRepos()
}