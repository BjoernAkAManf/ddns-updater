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
package com.sysorca.homelab.dyndns.cloudflare.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class URIBuilder {
    private final List<String> path = new ArrayList<>();
    private final Map<String, String> query = new HashMap<>();

    public URIBuilder(final String... path) {
        this.path.addAll(Arrays.asList(path));
    }

    public URIBuilder withPathSegments(final String... path) {
        this.path.addAll(List.of(path));
        return this;
    }

    public URIBuilder withQueryParam(final String key, final String value) {
        if (key.contains("&") || key.contains("?")) {
            throw new IllegalArgumentException("Invalid Key");
        }
        if (value.contains("&") || value.contains("?")) {
            throw new IllegalArgumentException("Invalid Value");
        }
        this.query.put(key, value);
        return this;
    }

    public URI build(final String base) {
        final var path = String.join("/", this.path);
        final var query = this.query.entrySet().stream()
                .map(i -> String.format("%s=%s", i.getKey(), i.getValue()))
                .collect(Collectors.joining("&", "?", ""));
        return URI.create(base + path + query);
    }
}
