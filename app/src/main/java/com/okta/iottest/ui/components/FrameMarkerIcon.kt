package com.okta.iottest.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.okta.iottest.ui.theme.ErrorContainer
import com.okta.iottest.ui.theme.OnErrorContainer
import com.okta.iottest.ui.theme.SemanticBrown10
import com.okta.iottest.ui.theme.SemanticBrown30
import com.okta.iottest.ui.theme.SemanticRed30

fun createBitmapWithBorder(drawableId: Any, context: Context, scaleFactor: Float = 1.5f, overlayDrawableId: Int, status : String?): Bitmap {
    // Get the original bitmap
    val originalBitmap = when (drawableId) {
        is Int -> (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap
        is Uri -> {
            val inputStream = context.contentResolver.openInputStream(drawableId)
            BitmapFactory.decodeStream(inputStream)
        }
        else -> throw IllegalArgumentException("Invalid type for drawableId")
    }

    // Scale the original bitmap
    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, (originalBitmap.width * scaleFactor).toInt(), (originalBitmap.height * scaleFactor).toInt(), false)

    // Get the overlay bitmap
    val overlayBitmap = (ContextCompat.getDrawable(context, overlayDrawableId) as BitmapDrawable).bitmap

    // Create a new bitmap with a border and a triangle
    val borderSize = 30
    val triangleHeight = 50
    val bitmapWithBorder = Bitmap.createBitmap(scaledBitmap.width + borderSize * 3, scaledBitmap.height + borderSize + triangleHeight, scaledBitmap.config)
    val canvas = Canvas(bitmapWithBorder)

    // Set the color based on the status
    val color = if (status == "fall") SemanticRed30.toArgb() else if (status == "help") SemanticBrown30.toArgb() else Color.Gray.toArgb()

    // Draw the border
    val borderPaint = Paint().apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = borderSize.toFloat()
        isAntiAlias = true
    }
    val borderRect = RectF(borderSize.toFloat(), borderSize.toFloat(), (scaledBitmap.width + borderSize).toFloat(), (scaledBitmap.height + borderSize).toFloat())
    canvas.drawRoundRect(borderRect, borderSize.toFloat(), borderSize.toFloat(), borderPaint)

    // Draw the bitmap
    val bitmapPaint = Paint().apply {
        isAntiAlias = true
    }
    val bitmapRect = RectF(borderSize.toFloat(), borderSize.toFloat(), (scaledBitmap.width + borderSize).toFloat(), (scaledBitmap.height + borderSize).toFloat())
    canvas.drawBitmap(scaledBitmap, null, bitmapRect, bitmapPaint)

    // Draw the overlay bitmap at the top right of the border
    val moveRight = 40 // adjust this value to move the overlay bitmap to the right
    val moveTop = 30 // adjust this value to move the overlay bitmap to the top
    val overlayRect = RectF((scaledBitmap.width + borderSize - overlayBitmap.width + moveRight).toFloat(), (borderSize - moveTop).toFloat(), (scaledBitmap.width + borderSize + moveRight).toFloat(), (borderSize + overlayBitmap.height - moveTop).toFloat())
    canvas.drawBitmap(overlayBitmap, null, overlayRect, bitmapPaint)

    // Draw the triangle
    val trianglePaint = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val trianglePath = Path().apply {
        moveTo(bitmapWithBorder.width / 2.3f, bitmapWithBorder.height.toFloat())
        lineTo(bitmapWithBorder.width / 2.3f - triangleHeight, bitmapWithBorder.height - triangleHeight.toFloat())
        lineTo(bitmapWithBorder.width / 2.3f + triangleHeight, bitmapWithBorder.height - triangleHeight.toFloat())
        close()
    }
    canvas.drawPath(trianglePath, trianglePaint)

    return bitmapWithBorder
}
