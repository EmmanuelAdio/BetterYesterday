package com.example.betteryesterday.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.betteryesterday.data.AppDatabase
import com.example.betteryesterday.data.AppRepository
import com.example.betteryesterday.data.Milestones
import kotlinx.coroutines.launch

class MilestoneViewModel(application: Application) : AndroidViewModel(application){
    private val repository : AppRepository

    val completedMilestones : LiveData<Int>
    val incompletedMilestones : LiveData<Int>

    init{
        val tablesDao = AppDatabase.getDatabase(application).tablesDao()
        repository = AppRepository(tablesDao)
        completedMilestones = repository.allCompleteMileStones
        incompletedMilestones = repository.allIncompleteMileStones
    }

    fun getGoalMilestones(id :Int) : LiveData<List<Milestones>> {
        return repository.getGoalMileStones(id)
    }

    fun insertMilestone(milestone : Milestones) = viewModelScope.launch {
        repository.insertNewMilestone(milestone)
    }

    fun deleteMilestone(milestone: Milestones)  = viewModelScope.launch {
        repository.deleteMilestone(milestone)
    }

    fun updateMilestone(milestone : Milestones) = viewModelScope.launch{
        repository.updateMilestone(milestone)
    }

    fun numOfGoalMilestone(id : Int) : LiveData<Int>{
        return repository.getNumOfMilestones(id)
    }

    fun numOfGoalIncomplete(id : Int) : LiveData<Int>{
        return repository.getNumOfIncomplete(id)
    }

    fun numOfGoalComplete(id : Int) : LiveData<Int>{
        return repository.getNumOfComplete(id)
    }
}