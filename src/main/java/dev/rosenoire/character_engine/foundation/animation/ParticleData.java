package dev.rosenoire.character_engine.foundation.animation;

import dev.rosenoire.character_engine.foundation.animation.scripting.AbstractValueGetter;
import net.collectively.geode.core.types.double3;

/// `position`<br/> Represents the base position of the particle, relative to the player's eye position. The particle will also rotate with the player's view keeping this offset from the player's eyes. Expects a three-dimensional value. For example:
/// ```json
/// {
///   "position": [1, 2, 3.45]
/// }
/// ```
///
/// `offset`<br/> A secondary position added to the main `position` field to add a potentially random offset easily. For example:
/// ```json
/// {
///   // Adds a random value between -0.5 and 0.5 on the X axis, 0 vertically and 1 on the Z axis.
///   "offset": [{"min": -0.5, "max": 0.5}, 0, 1]
/// }
/// ```
///
/// `velocity`<br/> In most cases, represents the speed at which the particle is moving and in what direction it is moving. For some particles, the `X`, `Y` or `Z` components of the velocity might be used to tweak other settings. For example:
/// ```json
/// {
///   // This particle goes up.
///   "velocity": [0, 1, 0]
/// }
/// ```
///
/// `count`<br/> Represents how many particles are spawned every time this keyframe is played. Requires an `Integer` value greater or equal to `1`. Providing a count of particles equal or lesser than `0` will prevent the particle from playing. For example:
/// ```json
/// {
///   // 20 particles will be spawned.
///   "count": 20
/// }
/// ```
public record ParticleData(
        AbstractValueGetter<double3> position,
        AbstractValueGetter<double3> velocity,
        AbstractValueGetter<double3> offset,
        AbstractValueGetter<Integer> count
) {
}
