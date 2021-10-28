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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public final class Request {
    private final API api;

    public Request(final HttpClient client, final String token) {
        this.api = new API(new ObjectMapper(), client, token);
    }

    public Zones zones(final String zoneId) {
        return new Zones(this.api, zoneId);
    }
}
