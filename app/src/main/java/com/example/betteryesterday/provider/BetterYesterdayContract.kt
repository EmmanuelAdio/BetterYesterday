package com.example.betteryesterday.provider

import android.net.Uri

object BetterYesterdayContract {
    const val AUTHORITY = "com.example.betteryesterday.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    object Goals {
        const val PATH_GOALS = "Goals"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GOALS)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_GOALS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_GOALS"

        // Column names for the Movies table
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DEADLINE = "deadline"
        const val COLUMN_DESCRIPTION = "description"
    }

    object Milestones {
        const val PATH_MILESTONES = "Milestones"

        // Base URI for milestones table
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MILESTONES)

        // MIME types for directory and single item
        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_MILESTONES"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_MILESTONES"

        // Column names
        const val COLUMN_MILESTONE_ID = "id"
        const val COLUMN_GOAL_ID = "goalID"
        const val COLUMN_SUMMARY = "summary"
        const val COLUMN_DEADLINE = "deadline"
        const val COLUMN_COMPLETE = "complete"
    }
}