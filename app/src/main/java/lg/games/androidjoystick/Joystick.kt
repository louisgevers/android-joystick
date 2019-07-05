package lg.games.androidjoystick

import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * This class represents the joystick logic. It contains the
 * coordinates for the joystick container and the stick itself.
 * Using these coordinates and the specified [radius], the
 * [angle] and the [strength] of the stick can be computed.
 *
 * @property radius the radius of the joystick container
 * @property centerX the x coordinate of the center of the joystick container
 * @property centerY the y coordinate of the center of the joystick container
 * @constructor Creates a joystick storing the given position and radius
 *
 * @author Louis Gevers
 */
class Joystick(var radius: Int, var centerX: Int, var centerY: Int) {

    /**
     * X coordinate of the stick.
     */
    var stickX: Int = centerX

    /**
     * Y coordinate of the stick.
     */
    var stickY: Int = centerY

    /**
     * Angle of the stick compared to the container.
     */
    val angle: Int
        get() = computeAngle()

    /**
     * Strength (i.e. relative distance) of the stick compared to
     * the container.
     */
    val strength: Int
        get() = computeStrength()

    /**
     * Computes and returns the angle of the stick in a 360 degree range.
     *
     * @return The angle of the joystick
     */
    private fun computeAngle(): Int {
        val angle = Math.toDegrees(atan2(
                stickY.toDouble() - centerY.toDouble(),
                stickX.toDouble() - centerX.toDouble())
        )
        return if (angle < 360) {
            angle.toInt() + 360
        } else {
            angle.toInt()
        }
    }

    /**
     * Computes and returns the relative strength of the stick in a
     * 0 to 100% range.
     *
     * @return The strength of the stick
     */
    private fun computeStrength(): Int {
        return 100 * computeDistance() / radius
    }

    /**
     * Computes and returns the distance between the stick and the center
     * of the container. This is used to compute the [strength] of the
     * stick.
     *
     * @return The distance between the stick and the center of the container
     */
    private fun computeDistance(): Int {
        return sqrt(
            (stickX.toDouble() - centerX.toDouble()).pow(2) +
                (stickY.toDouble() - centerY.toDouble()).pow(2)
        ).toInt()
    }

}