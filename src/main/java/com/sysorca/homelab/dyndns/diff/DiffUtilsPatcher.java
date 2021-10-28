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
package com.sysorca.homelab.dyndns.diff;

import com.github.difflib.DiffUtils;
import com.sysorca.homelab.dyndns.Planner;
import com.sysorca.homelab.dyndns.ServiceProvider;
import com.sysorca.homelab.dyndns.ServiceStorage;
import com.sysorca.homelab.dyndns.util.CRUDCounter;

import java.io.IOException;
import java.util.Objects;

public final class DiffUtilsPatcher implements Planner {
    @Override
    public CRUDCounter apply(final ServiceProvider source, final ServiceStorage target) throws IOException, InterruptedException {
        // TODO: Ordering collectServiceNames by some criteria (To avoid useless insert and delete statements)
        // Diff current DNS settings with expected DNS
        final var patch = DiffUtils.diff(
            target.collectServiceNames(),
            source.collectServiceNames(),
            (a, b) -> {
                if (!Objects.equals(a.getHost(), b.getHost())) {
                    return false;
                }
                return Objects.equals(a.getAddress(), b.getAddress());
            }
        );

        final var counter = new CRUDCounter();
        // Issue Create, Update, Delete Statements accordingly
        for (final var diff : patch.getDeltas()) {
            final var s = diff.getSource().getLines();
            final var t = diff.getTarget().getLines();
            switch (diff.getType()) {
                case EQUAL:
                    // Do nothing
                    continue;
                case INSERT:
                    counter.create();
                    target.insert(t);
                    continue;
                case CHANGE:
                    counter.update();
                    target.replace(s, t);
                    continue;
                case DELETE:
                    counter.delete();
                    target.delete(s);
            }
        }
        return counter;
    }
}
