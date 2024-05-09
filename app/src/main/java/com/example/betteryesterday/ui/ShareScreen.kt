package com.example.betteryesterday.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.betteryesterday.data.Goals
import com.example.betteryesterday.ui.viewModels.GoalViewModel
import com.example.betteryesterday.ui.viewModels.MilestoneViewModel

@Composable
fun ShareScreen(id: Int?, milestonesViewModel: MilestoneViewModel, goalViewModel: GoalViewModel) {
    /*This is the dashboard screen composable */
    //place holder add show that this is the dashboard screen.
    val context = LocalContext.current

    var goal = id?.let { goalViewModel.getGoal(it) }
    val goalState = goal?.observeAsState()?.value
    val entries = goalState?.let { getGoalPieChart(milestonesViewModel, it) }
    val entriesState = entries?.observeAsState()?.value

    val bitmap = capturePieChart(entriesState, context, goalState, milestonesViewModel)
    val imageUri = saveBitmapAsPng(bitmap, context)

    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally){
        item {
            Card(modifier = Modifier
                .width(400.dp)
                .height(300.dp),
                ){
                var progress = (entriesState?.get(0)?.percentage)?.times(100)
                Text(text = "For my goal : ${goalState?.title}\n" +
                        "goal Description : ${goalState?.description} \n" +
                        "goal Progress : $progress% complete\n ",
                    modifier = Modifier.padding(10.dp))
            }

        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Card(modifier = Modifier
                .width(200.dp)
                .height(200.dp)){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(25.dp)
                ){
                    if (entriesState != null) {
                        PieChart(entriesState)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    var progress = (entriesState?.get(0)?.percentage)?.times(100)
                    var message = "I am excited to share my total progress so far on my goal \n**${goalState?.title}**\n" +
                            "*goal Description* : ${goalState?.description} \n" +
                            "*goal Progress* : $progress% complete\n " +
                            "\n" +
                            "I am really enjoying my experience so far with the *BetterYesterday* app join me and download it too :D"

                    imageUri?.let {
                        shareImage(it, context, message)
                    }
                }) {
                    Icon(Icons.Filled.Share, contentDescription = "Share-sheet button")
                    Text(text = "Share")
                }
            }
        }
    }

}

@Composable
fun capturePieChart(
    entries: List<PieChartEntry>?,
    context: Context,
    goal: Goals?,
    milestonesViewModel: MilestoneViewModel
): Bitmap {
    /*This function changes the pie chart into a bitmap format so it can then be turned into a png/jpeg file and shared by the share sheet.*/
    // Adjust the bitmap size if needed
    val bitmapWidth = 800
    val bitmapHeight = 800
    val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888) // Adjust size as needed
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Calculate the center and radius of the pie chart
    val centerX = bitmapWidth / 2f
    val centerY = bitmapHeight / 2f
    val radius = Math.min(centerX, centerY) * 0.8f // Slightly smaller to fit within the bitmap

    // Define the bounding box for the pie segments
    val rect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

    // Calculate the start angles for each pie segment
    val startAngles = entries?.let { calculateStartAngles(it) }
    val gap = 1f // Gap between segments

    entries?.forEachIndexed { index, entry ->
        // Set the paint color for the segment
        paint.color = entry.color.toArgb()

        // Calculate the start angle and sweep angle for the segment
        val startAngle = startAngles!![index] + gap / 2
        val sweepAngle = entry.percentage * 360f - gap

        // Draw the arc representing the segment
        canvas.drawArc(rect, startAngle, sweepAngle, true, paint)
    }

    // Text properties
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE // Text color
        textSize = 50f // Text size
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Font style
    }

    // Draw text above the pie chart
    val text = "Total Progress on"
    val textWidth = textPaint.measureText(text)
    val textX = (bitmapWidth - textWidth) / 2
    val textY = 50f
    canvas.drawText(text, textX, textY, textPaint)

    val textTitle = "~${goal?.title}~"
    val textTitleWidth = textPaint.measureText(textTitle)
    val textTitleX = (bitmapWidth - textTitleWidth) / 2
    val textTitleY = textY + 50f
    canvas.drawText(textTitle, textTitleX, textTitleY, textPaint)

    Log.v("Entries", entries.toString())

    var progress = (entries?.get(0)?.percentage)?.times(100)

    // Text inside the pie chart
    val centerText = "$progress% complete!!"
    val centerTextWidth = textPaint.measureText(centerText)
    val centerTextX = (bitmapWidth - centerTextWidth) / 2
    val centerTextY = centerY
    canvas.drawText(centerText, centerTextX, centerTextY, textPaint)

    return bitmap
}

fun saveBitmapAsPng(bitmap: Bitmap, context: Context): Uri? {
    /*This function changes the saved bitmap into a png file image.
    * it returns a a */

    val filename = "pie_chart_${System.currentTimeMillis()}.png"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
        }
    }
    return uri
}

fun shareImage(uri: Uri, context: Context, message: String) {
    /*This is the share sheet intent that is used to share the user progress with other apps
    * TODO : Finish typing the message that is sent through the share sheet.*/


    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/*"

        // Add text and other extras
        putExtra(Intent.EXTRA_TEXT, message)
        putExtra(Intent.EXTRA_TITLE, "Share Goal Progress")

        // Grant permission to read the URI
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share PieChart via"))
}

