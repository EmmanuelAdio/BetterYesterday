package com.example.betteryesterday.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.database.getBlobOrNull
import com.example.betteryesterday.data.AppDatabase
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.data.Milestones
import com.example.betteryesterday.data.TablesDao

class BetterYesterdayProvider : ContentProvider() {
    private lateinit var database : TablesDao// use teh room database Data access object

    //this adds the CRUD methods
    override fun onCreate(): Boolean {
        // get an instance of the tableDao, to use the Room database
        database = AppDatabase.getDatabase(context!!).tablesDao()
        return true // Return true to indicate that the provider was successfully loaded
    }
    companion object {
        private const val GOALS = 100
        private const val GOALS_ID = 101

        private const val MILESTONES = 200
        private const val MILESTONES_ID = 201

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(BetterYesterdayContract.AUTHORITY, BetterYesterdayContract.Goals.PATH_GOALS, GOALS)
            addURI(BetterYesterdayContract.AUTHORITY, "${BetterYesterdayContract.Goals.PATH_GOALS}/#", GOALS_ID)
            addURI(BetterYesterdayContract.AUTHORITY, BetterYesterdayContract.Milestones.PATH_MILESTONES, MILESTONES)
            addURI(BetterYesterdayContract.AUTHORITY, "${BetterYesterdayContract.Milestones.PATH_MILESTONES}/#", MILESTONES_ID)
        }
    }

    override fun getType(uri: Uri): String? {
        // getType method to return the MIME type of data for the content URI
        return when (uriMatcher.match(uri)) {
            GOALS -> BetterYesterdayContract.Goals.CONTENT_TYPE
            GOALS_ID -> BetterYesterdayContract.Goals.CONTENT_ITEM_TYPE
            MILESTONES -> BetterYesterdayContract.Milestones.CONTENT_TYPE
            MILESTONES_ID -> BetterYesterdayContract.Milestones.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        //this is what handles multiple queries at onece
        val cursor : Cursor = when (uriMatcher.match(uri)){
            GOALS -> database.getAllGoals_CP()
            GOALS_ID -> {
                val id = ContentUris.parseId(uri)
                database.getGoalItem_CP(id.toInt())
            }
            MILESTONES -> database.getAllMilestones_CP()
            MILESTONES_ID -> {
                val id = ContentUris.parseId(uri).toInt()
                database.getMilestoneItem_CP(id)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val newID = when (uriMatcher.match(uri)) {
            GOALS -> {
                /*database.insertGoal(Goals.fromContentValues(contentValues))*/
                val newGoal = Goals(
                    title = contentValues?.getAsString(BetterYesterdayContract.Goals.COLUMN_TITLE) ?: "",
                    deadline = contentValues?.getAsString(BetterYesterdayContract.Goals.COLUMN_DEADLINE) ?: "12/12/2024",//if the user does not enter anything for the deadline we will automatically set it to the end of the year
                    description = contentValues?.getAsString(BetterYesterdayContract.Goals.COLUMN_DESCRIPTION) ?: ""
                )
                database.insertGoal_CP(newGoal)
            }
            //we are making it not possible for apps outside the better yesterday app be able to create milestones for goals
            MILESTONES -> throw UnsupportedOperationException("milestone insert operation is not supported")
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, newID)

    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val context = context ?: return 0  // Check if context is null
        val match = uriMatcher.match(uri)

        val count = when (match){
            GOALS_ID -> {
                val goalID = ContentUris.parseId(uri).toInt()
                // Retrieve the movie to be deleted
                val cursor = database.getGoalItem_CP(goalID)
                if (cursor.moveToFirst()) {
                    val goal = Goals(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    )
                    cursor.close()
                    // Use the DAO to delete the movie
                    database.deleteGoal_CP(goal)
                } else {
                    cursor.close()
                    0  // No movie found to delete
                }
            }

            MILESTONES_ID -> {
                val milestoneID = ContentUris.parseId(uri).toInt()
                // Retrieve the movie to be deleted
                val cursor = database.getMilestoneItem_CP(milestoneID)
                if (cursor.moveToFirst()) {
                    val milestone = Milestones(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        goalID = cursor.getInt(cursor.getColumnIndexOrThrow("goalID")),
                        summary = cursor.getString(cursor.getColumnIndexOrThrow("summary")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        complete = false//we are deleting this milestone anyway so it does not matter what state we delete it in :)
                    )
                    cursor.close()
                    // Use the DAO to delete the movie
                    database.deleteMilestone_CP(milestone)
                } else {
                    cursor.close()
                    0  // No movie found to delete
                }
            }
            else -> throw IllegalArgumentException("Deletion is not supported for $uri")
        }
        if (count != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return count
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        //we do not update anything in the better yesterday app :)
        throw UnsupportedOperationException("Update operation is not supported")
    }
}