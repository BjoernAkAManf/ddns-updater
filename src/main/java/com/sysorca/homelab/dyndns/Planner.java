package com.sysorca.homelab.dyndns;

import com.sysorca.homelab.dyndns.util.CRUDCounter;

import java.io.IOException;

public interface Planner {
    CRUDCounter apply(ServiceProvider source, ServiceStorage target) throws IOException, InterruptedException;
}
