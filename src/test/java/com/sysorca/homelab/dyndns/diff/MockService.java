package com.sysorca.homelab.dyndns.diff;

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.ThrowingFunction;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.function.IntFunction;

@Data
@RequiredArgsConstructor
public final class MockService implements Service {
    private static final int SIZE = 3;
    private static final IP[] SERVERS = create(IP[]::new, i -> IP.create("127.0.0." + i));
    private static final String[] HOSTS = create(String[]::new, i -> "host-" + i);
    private static final String[] SERVICES = create(String[]::new, i -> "service-" + i);
    private final String name;
    private final String host;
    private final IP address;

    public MockService(final int name, final int host, final int address) {
        this(SERVICES[name], HOSTS[host], SERVERS[address]);
    }

    private static <T> T[] create(final IntFunction<T[]> init, final ThrowingFunction<String, T> provider) {
        try {
            final var array = init.apply(SIZE);
            for (var i = 0; i < array.length; i += 1) {
                array[i] = provider.apply(String.valueOf(i));
            }
            return array;
        } catch (final Throwable ex) {
            throw new RuntimeException("Should never occur. Programming Error", ex);
        }
    }
}
