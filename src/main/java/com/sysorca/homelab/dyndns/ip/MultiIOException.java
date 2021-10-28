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

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Collection;

@Getter
@ToString
public final class MultiIOException extends IOException {
    private static final Throwable[] EMPTY_ARRAY = new Throwable[0];
    private final Throwable[] causes;

    public MultiIOException(final String msg, final Collection<? extends Throwable> causes) {
        this(msg, causes.size() > 0 ? causes.toArray(EMPTY_ARRAY) : EMPTY_ARRAY);
    }

    public MultiIOException(final String msg, final Throwable[] causes) {
        super(msg, causes.length > 0 ? causes[0] : null);
        this.causes = causes;
    }
}
