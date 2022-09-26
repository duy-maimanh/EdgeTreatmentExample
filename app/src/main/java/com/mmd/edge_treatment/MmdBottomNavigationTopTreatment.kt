package com.mmd.edge_treatment

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class MmdBottomNavigationTopTreatment : EdgeTreatment() {
    private val fabDiameter: Float
    private val offset: Float

    init {
        fabDiameter = 150f
        offset = 50f
    }

    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        if (fabDiameter == 0f) {
            // There is no cutout to draw.
            shapePath.lineTo(length, 0f)
            return
        }
        val fabMargin = 0f
        val cradleDiameter = fabMargin * 2 + fabDiameter
        val cradleRadius = cradleDiameter / 2f
        val roundedCornerRadius = 0f
        val roundedCornerOffset = interpolation * roundedCornerRadius
        val horizontalOffset = 0f
        val middle = center + horizontalOffset

        // The center offset of the cutout tweens between the vertical offset when attached, and the
        // cradleRadius as it becomes detached.
        val cradleVerticalOffset = 0f
        val verticalOffset =
            interpolation * cradleVerticalOffset + (1 - interpolation) * cradleRadius
        val verticalOffsetRatio = verticalOffset / cradleRadius
        if (verticalOffsetRatio >= 1.0f) {
            // Vertical offset is so high that there's no curve to draw in the edge, i.e., the fab is
            // actually above the edge so just draw a straight line.
            shapePath.lineTo(length, 0f)
            return  // Early exit.
        }

        // Calculate the path of the cutout by calculating the location of two adjacent circles. One
        // circle is for the rounded corner. If the rounded corner circle radius is 0 the corner will
        // not be rounded. The other circle is the cutout.

        // Calculate the X distance between the center of the two adjacent circles using pythagorean
        // theorem.
        val fabCornerSize = -1f
        val cornerSize = fabCornerSize * interpolation
        val arcOffset = 0f
        val distanceBetweenCenters = cradleRadius + roundedCornerOffset
        val distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters
        val distanceY = verticalOffset + roundedCornerOffset
        val distanceX =
            Math.sqrt((distanceBetweenCentersSquared - distanceY * distanceY).toDouble())
                .toFloat()

        // Calculate the x position of the rounded corner circles.
        val leftRoundedCornerCircleX = middle - distanceX
        val rightRoundedCornerCircleX = middle + distanceX

        // Calculate the arc between the center of the two circles.
        val cornerRadiusArcLength =
            Math.toDegrees(Math.atan((distanceX / distanceY).toDouble())).toFloat()
        val cutoutArcOffset = ARC_QUARTER - cornerRadiusArcLength + arcOffset

        // Draw the cutout circle.
        shapePath.addArc( /* left= */
            middle - (cradleRadius + offset),  /* top= */
            -cradleRadius - verticalOffset,  /* right= */
            middle + (cradleRadius + offset),  /* bottom= */
            (cradleRadius - verticalOffset) * 2,  /* startAngle= */
            ANGLE_LEFT + 20.0f,  /* sweepAngle= */
            ARC_HALF - 40.0f
        )
    }

    companion object {
        private const val ARC_QUARTER = 90
        private const val ARC_HALF = 180
        private const val ANGLE_UP = 270
        private const val ANGLE_LEFT = 180
    }
}
