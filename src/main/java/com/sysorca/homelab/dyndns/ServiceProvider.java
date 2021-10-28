package com.sysorca.homelab.dyndns;

import java.io.IOException;
import java.util.List;

public interface ServiceProvider {
    List<Service> collectServiceNames() throws IOException, InterruptedException;
}
