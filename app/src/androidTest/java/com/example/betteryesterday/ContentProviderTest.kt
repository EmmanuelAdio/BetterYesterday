package com.example.betteryesterday

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.betteryesterday.provider.BetterYesterdayContract
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

// Use AndroidJUnit4 runner: This is JUnit4 with special Android extensions.
@RunWith(AndroidJUnit4::class)
class BetterYesterdayProviderTest {

    private lateinit var context: Context // Context of the app under test.
    private lateinit var resolver: ContentResolver // ContentResolver to interact with the ContentProvider.

    @Before
    fun setUp() {
        // Set up the environment for each test.
        context = InstrumentationRegistry.getInstrumentation().targetContext // Get the context.
        resolver = context.contentResolver // Get the ContentResolver from the context.
    }

    @Test
    fun testInsertAndQueryGoal() {
        // Test inserting and querying goals.

        // Create a ContentValues object to hold the goal data.
        val goalValues = ContentValues().apply {
            put(BetterYesterdayContract.Goals.COLUMN_TITLE, "Learn Android")
            put(BetterYesterdayContract.Goals.COLUMN_DESCRIPTION, "Study Android development deeply")
            put(BetterYesterdayContract.Goals.COLUMN_DEADLINE, "31/12/2024")
        }

        // Insert the goal into the provider using the ContentResolver. Expect a URI back.
        val goalUri = resolver.insert(BetterYesterdayContract.Goals.CONTENT_URI, goalValues)
        assertNotNull("Failed to insert goal", goalUri) // Make sure the insertion didn't fail.

        // Query the goals from the provider. Expect a Cursor back.
        val cursor = resolver.query(
            BetterYesterdayContract.Goals.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Ensure the cursor is not null and has at least one record.
        assertNotNull("Cursor is null", cursor)
        assertTrue("Cursor is empty", cursor!!.moveToFirst())

        // Read the title from the cursor and check if it matches the inserted value.
        val titleIndex = cursor.getColumnIndex(BetterYesterdayContract.Goals.COLUMN_TITLE)
        val title = cursor.getString(titleIndex)
        assertEquals("Title doesn't match", "Learn Android", title)

        // Close the cursor to release resources.
        cursor.close()
    }

    @Test
    fun testDeleteGoal() {
        // Test deleting a goal.

        // Insert a goal to be deleted.
        val goalValues = ContentValues().apply {
            put(BetterYesterdayContract.Goals.COLUMN_TITLE, "Run a marathon")
            put(BetterYesterdayContract.Goals.COLUMN_DESCRIPTION, "Prepare for a marathon in 2024")
            put(BetterYesterdayContract.Goals.COLUMN_DEADLINE, "01/06/2024")
        }

        // Insert the goal and get its URI.
        val goalUri = resolver.insert(BetterYesterdayContract.Goals.CONTENT_URI, goalValues)
        assertNotNull("Failed to insert goal before deletion", goalUri)

        // Delete the goal by its URI.
        val deleteCount = resolver.delete(goalUri!!, null, null)
        assertEquals("Goal deletion failed", 1, deleteCount)

        // Query the deleted goal to ensure it's really gone.
        val cursor = resolver.query(
            goalUri,
            null,
            null,
            null,
            null
        )

        // Check that the cursor is empty (goal is deleted).
        assertNotNull("Cursor is null after delete", cursor)
        assertFalse("Cursor is not empty after delete", cursor!!.moveToFirst())

        // Close the cursor.
        cursor.close()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testInsertMilestone() {
        // Test that inserting a milestone throws UnsupportedOperationException.

        // Define milestone data.
        val milestoneValues = ContentValues().apply {
            put(BetterYesterdayContract.Milestones.COLUMN_GOAL_ID, 1)
            put(BetterYesterdayContract.Milestones.COLUMN_SUMMARY, "First 5k run")
            put(BetterYesterdayContract.Milestones.COLUMN_COMPLETE, false)
        }

        // Attempt to insert a milestone, expecting an exception.
        resolver.insert(BetterYesterdayContract.Milestones.CONTENT_URI, milestoneValues)
    }

    @Test
    fun testQueryAllGoals() {
        // Test querying all goals.

        // Query all goals from the provider.
        val cursor = resolver.query(
            BetterYesterdayContract.Goals.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        // Ensure the cursor is not null and not empty.
        assertNotNull("Cursor is null when querying all goals", cursor)
        assertTrue("Cursor is empty when querying all goals", cursor!!.moveToFirst())

        // Iterate over the cursor and check the contents.
        do {
            val id = cursor.getInt(cursor.getColumnIndex(BetterYesterdayContract.Goals.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndex(BetterYesterdayContract.Goals.COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndex(BetterYesterdayContract.Goals.COLUMN_DESCRIPTION))
            val targetDate = cursor.getString(cursor.getColumnIndex(BetterYesterdayContract.Goals.COLUMN_DEADLINE))

            // Ensure that the values are valid.
            assertTrue("Invalid ID", id > 0)
            assertNotNull("Title is null", title)
            assertNotNull("Description is null", description)
            assertNotNull("Target date is null", targetDate)
        } while (cursor.moveToNext())

        // Close the cursor.
        cursor.close()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        // Test querying an invalid URI which should throw IllegalArgumentException.

        // Query an invalid URI.
        resolver.query(Uri.parse("content://com.example.betteryesterday.provider/invalid"), null, null, null, null)
    }

//    @After
//    fun tearDown() {
//        // Clean up after tests if needed. This is a good place to clear any test data if necessary.
//    }
}
