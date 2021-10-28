package com.sysorca.homelab.dyndns;

import com.sysorca.homelab.dyndns.util.IP;

import java.io.IOException;

/**
 * Service Extension Point: Retrieves the external IP of the client.
 */
public interface IPFetcher {
    /**
     * Requests the external Address.
     *
     * @return External IP
     * @throws IOException If an error occurred during the Request
     */
    IP retrieveIP() throws IOException;
}
