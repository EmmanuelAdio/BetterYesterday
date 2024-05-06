package com.example.betteryesterday.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/*These are teh entity tables that will be used ot store the data in our database.*/
@Entity(tableName = "Goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val title: String,
    val deadline: String,
    val description : String
)

@Entity(tableName = "Milestones", foreignKeys =
    [ForeignKey(entity = Goals::class, parentColumns = ["id"], childColumns = ["goalID"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
])
data class Milestones(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val goalID: Int = 0,
    val summary: String,
    val deadline: String,
    val complete: Boolean
)