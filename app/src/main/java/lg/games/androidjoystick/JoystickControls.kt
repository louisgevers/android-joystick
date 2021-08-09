package lg.games.androidjoystick

import android.view.MotionEvent

/**
 * This class is responsible for handling events and propagating them
 * to the [Joystick] object. It offers the user interaction with
 * the joystick.
 *
 * @property joystick the joystick object to control.
 * @constructor Creates the controls object with specified joystick
 *
 * @author Louis Gevers
 */
class JoystickControls(private val joystick: Joystick) {

    /**
     * Handles the given event by updating the stick position accordingly.
     * If the event is a release event the stick's position is set back
     * to the center. Otherwise the stick's position is moved towards the
     * event's position, and capped at the border of the joystick container.
     *
     * @param event The event to process
     */
    fun handleEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_UP) {
            resetPosition()
        } else {
            joystick.stickX = when(joystick.behavior){
                Joystick.Behaviour.Vertical -> joystick.centerX
                else -> event.x.toInt()
            }
            joystick.stickY = when(joystick.behavior){
                Joystick.Behaviour.Horizontal -> joystick.centerY
                else -> event.y.toInt()
            }
            val distance = joystick.computeDistance()
            if (distance > joystick.radius) {
                capStickToBorder(distance)
            }
        }
    }

    /**
     * Resets the position of the joystick to the center position.
     */
    private fun resetPosition() {
        joystick.stickX = joystick.centerX
        joystick.stickY = joystick.centerY
    }

    /**
     * Caps the stick's position to the border of the container.
     */
    private fun capStickToBorder(distance: Int) {
        joystick.stickX =
            (joystick.stickX - joystick.centerX) * joystick.radius / distance + joystick.centerX
        joystick.stickY =
            (joystick.stickY - joystick.centerY) * joystick.radius / distance + joystick.centerY
    }

}