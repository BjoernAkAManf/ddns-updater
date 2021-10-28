package com.sysorca.homelab.dyndns.ip;

import com.sysorca.homelab.dyndns.IPFetcher;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Implements {@link IPFetcher} by delegating the resolution to child fetchers.
 * Each fetcher will be called in their iteration order.
 * If successful the result will be returned. Otherwise, the next potential resolver will be used.
 */
@Slf4j
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FallbackFetcher implements IPFetcher {
    private final Collection<IPFetcher> fetchers;

    /**
     * Creates a new Instance.
     *
     * @param fetchers Array of fetchers
     */
    public FallbackFetcher(final IPFetcher... fetchers) {
        this(Arrays.asList(fetchers));
    }

    @Override
    public IP retrieveIP() throws IOException {
        final var exceptions = new ArrayList<IOException>(this.fetchers.size());
        for (final var fetcher : this.fetchers) {
            try {
                return fetcher.retrieveIP();
            } catch (final IOException ex) {
                exceptions.add(ex);
                log.warn("Error while retrieving IP using {}", fetcher.getClass(), ex);
            }
        }
        throw new MultiIOException("Fetchers have been exhausted and all failed", exceptions);
    }
}
