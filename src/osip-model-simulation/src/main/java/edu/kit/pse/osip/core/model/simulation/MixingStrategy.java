package edu.kit.pse.osip.core.model.simulation;

import edu.kit.pse.osip.core.model.base.Liquid;
import java.util.LinkedList;

/**
 * A strategy to mix liquids. This includes the mixing of colors and the mixing of temperatures.
 *
 * @author Hans-Peter Lehmann
 * @version 1.0
 */
public interface MixingStrategy {
    /**
     * Mixes the given liquids and generates a new one. Volume equals the sum of all others.
     * Colors and temperatures get mixed pro rata.
     *
     * @return a single Liquid element containing a mixture of the given liquids.
     * @param inflow The different liquids to mix.
     */
    Liquid mixLiquids(LinkedList<Liquid> inflow);
}
