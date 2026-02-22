package com.horob1.geezo.core.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.toArgb
import android.graphics.BlurMaskFilter
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.nativeCanvas


fun Modifier.customShadow(
    color: Color,
    blur: Dp,
    offsetX: Dp,
    offsetY: Dp,
    shape: Shape = RoundedCornerShape(0.dp)
): Modifier = this.then(
    Modifier.drawBehind {

        Paint().asFrameworkPaint().apply {
            this.color = color.toArgb()
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.save()

            canvas.nativeCanvas.translate(
                offsetX.toPx(),
                offsetY.toPx()
            )

            val outline = shape.createOutline(size, layoutDirection, this)

            drawOutline(
                outline = outline,
                color = color
            )

            canvas.nativeCanvas.restore()
        }
    }
)

// Background blur
fun Modifier.backgroundBlur(
    radius: Dp = 20.dp
): Modifier {
    return this.blur(radius)
}