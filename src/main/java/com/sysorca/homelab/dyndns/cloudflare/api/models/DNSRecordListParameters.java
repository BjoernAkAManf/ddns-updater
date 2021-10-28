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
package com.sysorca.homelab.dyndns.cloudflare.api.models;

import lombok.Builder;
import lombok.Data;

import java.util.function.BiConsumer;

@Data
@Builder
public final class DNSRecordListParameters {
    @Builder.Default
    private final Integer page = null;

    @Builder.Default
    private final Integer perPage = null;

    public void buildQueryParams(final BiConsumer<String, String> append) {
        if (page != null) {
            append.accept("page", this.page.toString());
        }
        if (perPage != null) {
            append.accept("per_page", this.perPage.toString());
        }
    }

    public boolean hasPageSet() {
        return this.page != null;
    }

    public boolean hasPerPageSet() {
        return this.page != null;
    }
}
