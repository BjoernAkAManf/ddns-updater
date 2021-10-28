package com.sysorca.homelab.dyndns.docker;

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.Data;

@Data
public final class DockerService implements Service {
    private final String name;
    private final String host;
    private final IP address;
}
