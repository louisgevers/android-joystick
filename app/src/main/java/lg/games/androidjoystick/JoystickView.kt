package lg.games.androidjoystick

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

/**
 * This class represents a virtual joystick that can be used as a view
 * component in android.
 *
 * @constructor Creates a view with given attributes and a [Joystick] object.
 *
 * @author Louis Gevers
 */
class JoystickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * The joystick object to handle the logic.
     */
    private val joystick = Joystick(100, 0, 0, 0.2)

    /**
     * The graphics object to render the joystick.
     */
    private val graphics = JoystickGraphics(joystick)

    /**
     * The controls object to move the joystick.
     */
    private val controls = JoystickControls(joystick)

    /**
     * Draws the joystick's container and stick at their specified
     * locations.
     *
     * @param canvas Canvas on which to draw the joystick
     */
    override fun onDraw(canvas: Canvas?) {
        canvas?.let { graphics.draw(it) }
    }

    /**
     * Determines the size requirements for the joystick. This sets the measurements
     * such that the joystick fits into the specified width and height by taking the
     * smallest of these two as radius.
     *
     * @param widthMeasureSpec Horizontal space requirement imposed by the parent
     * @param heightMeasureSpec Vertical space requirement imposed by the parent
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = graphics.determineMeasurement(widthMeasureSpec)
        val height = graphics.determineMeasurement(heightMeasureSpec)
        val measurement = min(width, height)
        setMeasuredDimension(measurement, measurement)
    }

    /**
     * Resizes and repositions the joystick according to the new width and height.
     * This method is called when the size of the view has changed.
     *
     * @param w Current width of the view
     * @param h Current height of the view
     * @param oldw Old width of the view
     * @param oldh Old height of the view
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        graphics.changeSize(w, h)
    }

    /**
     * Handles touch actions to update the position of the stick accordingly.
     *
     * @param event The motion event that was fired
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { controls.handleEvent(it) }
        invalidate()
        return true
    }

}