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
 * @property stickRatio ratio of the radius of the stick compared to the ratio of the container.
 * @constructor Creates a joystick storing the given position and radius
 *
 * @author Louis Gevers
 */
class Joystick(var radius: Int, var centerX: Int, var centerY: Int, var stickRatio: Double) {

    /**
     * Represents the different directions the stick can point towards.
     */
    enum class Direction {
        CENTER,
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST
    }

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
     * Radius of the stick. This value is always less or equal to
     * [radius].
     */
    val stickRadius: Int
        get() = computeStickRadius()

    /**
     * Direction towards which the stick is pointing.
     */
    val direction: Direction
        get() = determineDirection()

    /**
     * Computes and returns the angle of the stick in a 360 degree range.
     *
     * @return The angle of the joystick
     */
    private fun computeAngle(): Int {
        val angle = Math.toDegrees(atan2(
                centerY.toDouble() - stickY.toDouble(),
                stickX.toDouble() - centerX.toDouble())
        )
        return if (angle < 0) {
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
    fun computeDistance(): Int {
        return sqrt(
            (stickX.toDouble() - centerX.toDouble()).pow(2) +
                (stickY.toDouble() - centerY.toDouble()).pow(2)
        ).toInt()
    }

    /**
     * Compute the radius of the stick based on the [stickRatio] and
     * the [radius] of the container.
     */
    private fun computeStickRadius(): Int {
        val res = stickRatio * radius
        return if (res > radius) {
            radius
        } else {
            res.toInt()
        }
    }

    /**
     * Determines the direction of the stick given the angle.
     *
     * @return The direction of the stick
     */
    private fun determineDirection(): Direction {
        if (strength == 0) {
            return Direction.CENTER
        }
        return when (angle) {
            in 0..22, in 338..360 -> Direction.EAST
            in 23..67 -> Direction.NORTH_EAST
            in 68..112 -> Direction.NORTH
            in 113..157 -> Direction.NORTH_WEST
            in 158..202 -> Direction.WEST
            in 203..247 -> Direction.SOUTH_WEST
            in 248..292 -> Direction.SOUTH
            in 293..337 -> Direction.SOUTH_EAST
            else -> Direction.CENTER
        }
    }

}