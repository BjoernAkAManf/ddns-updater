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
