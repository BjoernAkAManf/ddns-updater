/*
 * Copyright 2021 sysorca.com and contributors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
