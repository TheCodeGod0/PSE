package edu.kit.pse.osip.core.opcua.client;

/**
 * Listener that is called when a float value was loaded from the server.
 *
 * @author Hans-Peter Lehmann
 * @version 1.0
 */
@FunctionalInterface
public interface FloatReceivedListener extends ReceivedListener {
    /**
     * A float was received from the server.
     * 
     * @param value The received value.
     */
    void onReceived(float value);
}
