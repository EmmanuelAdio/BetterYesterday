package com.example.betteryesterday.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TablesDao{
    /*This is the table DAO that we will be using to access all of the table in our database*/

    /*Goals Operations*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGoal (goals: Goals)

    @Update
    suspend fun updateGoal(goals: Goals)

    @Delete
    suspend fun deleteGoal(goals: Goals)

    @Query("SELECT * from Goals WHERE id = :id")
    fun getGoalItem(id : Int) : LiveData<Goals>

    @Query("SELECT * FROM Goals")
    fun getAllGoals(): LiveData<List<Goals>>

    /* Milestone operations */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMilestone(milestones: Milestones)

    @Update
    suspend fun updateMilestone(milestones: Milestones)

    @Delete
    suspend fun deleteMilestone(milestones: Milestones)

    @Query("SELECT * from Milestones WHERE id = :id")
    fun getMilestoneItem(id: Int) : LiveData<Milestones>

    @Query("SELECT * from Milestones WHERE goalID = :id")
    fun getGoalMilestones(id: Int) : LiveData<List<Milestones>>

    @Query("SELECT COUNT(*) FROM Milestones WHERE complete = true")
    fun getNumOfComplete() : LiveData<Int>

    @Query("SELECT COUNT(*) FROM Milestones WHERE complete = false")
    fun getNumOfIncomplete() : LiveData<Int>

    @Query("UPDATE milestones SET complete = true WHERE id = :id")
    suspend fun completeMilestone(id: Int)

    @Query("UPDATE milestones SET complete = false WHERE id = :id")
    suspend  fun incompleteMilestone(id: Int)
}