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

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.Data;

@Data
public final class CFService implements Service {
    private final String id;
    private final String host;
    private final IP address;

    @Override
    public String getName() {
        return this.id;
    }
}
