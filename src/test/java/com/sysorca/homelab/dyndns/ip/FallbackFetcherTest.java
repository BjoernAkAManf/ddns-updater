package com.sysorca.homelab.dyndns.ip;

import com.sysorca.homelab.dyndns.IPFetcher;
import com.sysorca.homelab.dyndns.util.IP;
import com.sysorca.homelab.dyndns.util.IPV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public final class FallbackFetcherTest {
    @Test
    public void throwsNotIfOneSucceeds() throws IOException {
        final IPFetcher thrower = () -> {
            throw new IOException("No error occurs");
        };
        final var f = new FallbackFetcher(
            thrower,
            thrower,
            () -> IP.create("127.0.0.1")
        );
        final var ip = f.retrieveIP();
        Assertions.assertEquals("127.0.0.1", ip.getAddress());
        Assertions.assertEquals(IPV.V4, ip.getVersion());
    }

    @Test
    public void catchesMultipleException() {
        final var ex = new IOException("An error occurs");
        final IPFetcher thrower = () -> {
            throw ex;
        };
        final var f = new FallbackFetcher(
            thrower,
            thrower,
            thrower
        );
        final var m = Assertions.assertThrows(MultiIOException.class, f::retrieveIP);
        Assertions.assertArrayEquals(
            new Throwable[]{
                ex, ex, ex
            },
            m.getCauses()
        );
    }
}
