package com.example.betteryesterday.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.betteryesterday.data.AppDatabase
import com.example.betteryesterday.data.AppRepository
import com.example.betteryesterday.data.Goals
import kotlinx.coroutines.launch

class GoalViewModel(application: Application) : AndroidViewModel(application){
    private val repository : AppRepository

    val allGoals : LiveData<List<Goals>>
    init{
        val tablesDao = AppDatabase.getDatabase(application).tablesDao()
        repository = AppRepository(tablesDao)
        allGoals = repository.allGoals
    }
    fun getGoal(goalID: Int) : LiveData<Goals> {
        return repository.getGoalItem(goalID)
    }

    fun insertGoal(goal : Goals) = viewModelScope.launch {
        repository.insertNewGoal(goal)
    }

    fun deleteGoal(goal: Goals)  = viewModelScope.launch {
        repository.deleteGoal(goal)
    }

    fun updateGoal(goal : Goals) = viewModelScope.launch{
        repository.updateGoal(goal)
    }


}