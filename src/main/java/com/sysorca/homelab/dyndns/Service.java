package com.sysorca.homelab.dyndns;

import com.sysorca.homelab.dyndns.util.IP;

public interface Service {
    String getName();

    String getHost();

    IP getAddress();
}
