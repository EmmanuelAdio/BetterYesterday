package com.example.betteryesterday.data

import androidx.lifecycle.LiveData

class AppRepository(private val tablesDao: TablesDao){
    /*Goal repository operations*/
    val allGoals: LiveData<List<Goals>> = tablesDao.getAllGoals()
    fun getGoalItem(id :Int): LiveData<Goals> {
        return tablesDao.getGoalItem(id)
    }

    suspend fun insertNewGoal(goal : Goals){
        tablesDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal : Goals){
        tablesDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal : Goals){
        tablesDao.deleteGoal(goal)
    }


    /*Milestone repository operations*/
    suspend fun insertNewMilestone(milestones: Milestones){
        tablesDao.insertMilestone(milestones)
    }

    suspend fun updateMilestone(milestones: Milestones){
        tablesDao.updateMilestone(milestones)
    }

    suspend fun deleteMilestone(milestones: Milestones){
        tablesDao.deleteMilestone(milestones)
    }

    fun getGoalMileStones(id : Int) : LiveData<List<Milestones>>{
        return tablesDao.getGoalMilestones(id)
    }
}