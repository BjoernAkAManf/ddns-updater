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
package com.sysorca.homelab.dyndns.cloudflare;

import com.sysorca.homelab.dyndns.cloudflare.api.Zones;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecord;
import com.sysorca.homelab.dyndns.cloudflare.api.models.DNSRecordListParameters;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class Loader {
    private final int perPage;
    private Integer count;
    private Integer pages;

    public static void load(final int perPage, final Zones zones, final Consumer<DNSRecord[]> emitter) throws IOException, InterruptedException {
        new Loader(perPage).load(zones, emitter);
    }

    private void load(final Zones zones, final Consumer<DNSRecord[]> emitter) throws IOException, InterruptedException {
        synchronized (this) {
            this.reset();
            loadDNSRecord(zones, 1, emitter);
            for (int page = 2; page <= this.pages; page += 1) {
                loadDNSRecord(zones, page, emitter);
            }
            this.reset();
        }
    }

    private void reset() {
        this.count = null;
        this.pages = null;
    }

    private void loadDNSRecord(final Zones zones, final int page, final Consumer<DNSRecord[]> emitter) throws IOException, InterruptedException {
        final var params = DNSRecordListParameters.builder()
                .page(page)
                .perPage(this.perPage)
                .build();
        final var resp = zones.getDNSRecords(params);
        final var pagination = resp.getPagination();
        if (pagination.getPage() != params.getPage()) {
            // Should never occur
            throw new IllegalStateException("Invalid page provided");
        }
        if (pagination.getPerPage() != params.getPerPage()) {
            // Should never occur (?)
            throw new IllegalStateException("Invalid per_page provided");
        }
        final var result = resp.getResult();
        if (result.length != pagination.getCount()) {
            throw new IllegalStateException("Invalid response: Count is not equal to the amount of items provided!");
        }
        if (this.count == null) {
            this.count = pagination.getTotalCount();
        }
        if (this.pages == null) {
            this.pages = pagination.getTotalPages();
        }
        if (this.count != pagination.getTotalCount()) {
            throw new IllegalStateException("Count has changed. Pagination failed!");
        }
        if (this.pages != pagination.getTotalPages()) {
            throw new IllegalStateException("Page size has changed. Pagination failed!");
        }
        emitter.accept(result);
    }
}
