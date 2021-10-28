package com.sysorca.homelab.dyndns.cloudflare;

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.Data;

@Data
public final class CFService implements Service {
    private final String id;
    private final String host;
    private final IP address;

    @Override
    public String getName() {
        return this.id;
    }
}
