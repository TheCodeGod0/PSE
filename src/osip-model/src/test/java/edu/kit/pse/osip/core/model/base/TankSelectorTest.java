package edu.kit.pse.osip.core.model.base;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class to test the TankSelector class.
 *
 * @author David Kahles
 * @version 1.0
 */
public class TankSelectorTest {
    /**
     * Checks whether valuesWithoutMix() returns all enum values except the mix tank.
     */
    @Test
    public void testValuesWithoutMix() {
        TankSelector[] values = TankSelector.values();
        TankSelector[] valuesWithoutMix = TankSelector.valuesWithoutMix();
        assertEquals(values.length, valuesWithoutMix.length + 1);
        for (TankSelector i : valuesWithoutMix) {
            assertFalse(i == TankSelector.MIX);
        }
    }

    /**
     * Checks whether getUpperTankCount() returns one less than the length of values().
     */
    @Test
    public void testGetUpperTankCount() {
        assertEquals(TankSelector.getUpperTankCount(), TankSelector.valuesWithoutMix().length);
    }

    /**
     * Checks whether there are at least two tanks defined, one of them being the mix tank.
     */
    @Test
    public void testGEnoughTanks() {
        /* TankSelector.MIX exists */
        TankSelector testSelector = TankSelector.MIX;
        assertTrue(TankSelector.valuesWithoutMix().length >= 2);
        assertTrue(TankSelector.values().length >= 3);
    }

    /**
     * Checks whether getInitialColor() and getInitialTemperature() returns a value for every tank. Should not throw an
     * ArrayIndexOutOfBoundsException.
     */
    @Test
    public void testGetInitialColorTemperature() {
        for (TankSelector selector: TankSelector.values()) {
            selector.getInitialColor();
            selector.getInitialTemperature();
        }
    }
}
