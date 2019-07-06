package lg.games.androidjoystick

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import kotlin.math.min

/**
 * This class is responsible for rendering a [Joystick] on a canvas.
 * It also takes care of resizing and repositioning the joystick
 * when needed.
 *
 * @property joystick the joystick object to draw and resize.
 * @constructor Creates the graphics object with specified joystick
 *
 * @author Louis Gevers
 */
class JoystickGraphics(private val joystick: Joystick) {

    /**
     * Default size of the joystick if the measureSpec is unspecified.
     * See [determineMeasurement].
     */
    private val DEFAULT_SIZE = 200

    /**
     * Paint object used to paint the container of the joystick.
     */
    val containerPaint = Paint()

    /**
     * Paint object used to paint the border of the container of the joystick.
     */
    val containerBorderPaint = Paint()

    /**
     * Paint object used to paint the stick of the joystick.
     */
    val stickPaint = Paint()

    /**
     * Paint object used to paint the border of the stick of the joystick.
     */
    val stickBorderPaint = Paint()

    /**
     * Initialize the paint objects.
     */
    init {
        containerBorderPaint.style = Paint.Style.STROKE
        containerBorderPaint.isAntiAlias = true
        containerBorderPaint.isDither = true
        stickBorderPaint.style = Paint.Style.STROKE
        stickBorderPaint.isAntiAlias = true
        stickBorderPaint.isDither = true
    }

    /**
     * Draws the [joystick] on the given canvas.
     *
     * @param canvas The canvas on which to draw
     */
    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            joystick.centerX.toFloat(),
            joystick.centerY.toFloat(),
            joystick.radius.toFloat(),
            containerPaint)
        canvas.drawCircle(
            joystick.centerX.toFloat(),
            joystick.centerY.toFloat(),
            joystick.radius.toFloat() - containerBorderPaint.strokeWidth / 2,
            containerBorderPaint
        )
        canvas.drawCircle(
            joystick.stickX.toFloat(),
            joystick.stickY.toFloat(),
            joystick.stickRadius.toFloat(),
            stickPaint)
        canvas.drawCircle(
            joystick.stickX.toFloat(),
            joystick.stickY.toFloat(),
            joystick.stickRadius.toFloat() - stickBorderPaint.strokeWidth / 2,
            stickBorderPaint)
    }

    /**
     * Determines the measurement of the stick given the measureSpec. If the
     * measureSpec is unspecified, the [DEFAULT_SIZE] is returned, else the
     * specified size (i.e. all of it) is returned.
     *
     * @param measureSpec Specifies the desired measurement
     * @return The measurement based on the given measureSpec
     */
    fun determineMeasurement(measureSpec: Int): Int {
        return if (measureSpec == View.MeasureSpec.UNSPECIFIED) {
            DEFAULT_SIZE
        } else {
            View.MeasureSpec.getSize(measureSpec)
        }
    }

    /**
     * Changes the size and position of the joystick given the specified
     * width and height. It does so by centering the joystick and making
     * the radius as large as possible.
     *
     * @param width New width for the joystick
     * @param height New height for the joystick
     */
    fun changeSize(width: Int, height: Int) {
        joystick.centerX = width / 2
        joystick.centerY = height / 2
        joystick.stickX = joystick.centerX
        joystick.stickY = joystick.centerY
        joystick.radius = ((min(width, height) / 2) / (1 + joystick.stickRatio)).toInt()
    }

}