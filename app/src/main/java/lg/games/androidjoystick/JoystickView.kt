package lg.games.androidjoystick

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
     * This interface represents the callback to be invoked when the
     * user interacts with the joystick.
     */
    interface OnMoveListener {

        /**
         * Callback function when the user interacts with the joystick.
         *
         * @param angle Angle at which the stick is
         * @param strength Strength (or relative distance) of the stick
         * @param direction Direction of the stick, corresponding to the [angle]
         */
        fun onMove(angle: Int, strength: Int, direction: Joystick.Direction)
    }

    /**
     * The joystick object to handle the logic.
     */
    private val joystick = Joystick(100, 0, 0, 0.3)

    /**
     * The graphics object to render the joystick.
     */
    private val graphics = JoystickGraphics(joystick)

    /**
     * The controls object to move the joystick.
     */
    private val controls = JoystickControls(joystick)

    /**
     * Listener object for processing user interaction with the joystick.
     */
    var onMoveListener: OnMoveListener? = null

    /**
     * Sets up the components and joystick with the supplied or default attributes.
     */
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JoystickView,
            0, 0).apply {
            try {
                graphics.containerPaint.color = getColor(R.styleable.JoystickView_backgroundColor, Color.LTGRAY)
                graphics.stickPaint.color = getColor(R.styleable.JoystickView_foregroundColor, Color.DKGRAY)
            } finally {
                recycle()
            }
        }
    }

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
        onMoveListener?.onMove(joystick.angle, joystick.strength, joystick.direction)
        invalidate()
        return true
    }

}