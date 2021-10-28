package com.sysorca.homelab.dyndns;

import java.io.IOException;
import java.util.List;

public interface ServiceStorage extends ServiceProvider {
    void insert(List<Service> services) throws IOException, InterruptedException;

    void replace(List<Service> source, List<Service> target) throws IOException, InterruptedException;

    void delete(List<Service> services) throws IOException, InterruptedException;
}
