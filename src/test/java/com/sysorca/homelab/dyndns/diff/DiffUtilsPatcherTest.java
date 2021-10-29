package com.sysorca.homelab.dyndns.diff;

import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.ServiceProvider;
import com.sysorca.homelab.dyndns.ServiceStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

public final class DiffUtilsPatcherTest {
    @Test
    public void testNOOP() throws IOException, InterruptedException {
        final var one = new MockService(0, 0, 0);
        final var two = new MockService(1, 0, 0);

        this.run(
            // Input
            List.of(one, two),
            List.of(one, two),
            // Expected Output
            List.of(),
            0,
            List.of(),
            List.of()
        );
    }

    @Test
    public void testCreate() throws IOException, InterruptedException {
        final var one = new MockService(0, 0, 0);
        final var two = new MockService(1, 0, 0);

        this.run(
            // Input
            List.of(one, two),
            List.of(one),
            // Expected Output
            List.of(List.of(two)),
            0,
            List.of(),
            List.of()
        );
    }

    @Test
    public void testDeletes() throws IOException, InterruptedException {
        final var one = new MockService(0, 0, 0);
        final var two = new MockService(1, 0, 0);

        this.run(
            // Input
            List.of(one),
            List.of(one, two),
            // Expected Output
            List.of(),
            0,
            List.of(),
            List.of(List.of(two))
        );
    }

    @Test
    public void testReplaceHost() throws IOException, InterruptedException {
        final var one = new MockService(0, 0, 0);
        final var two = new MockService(0, 1, 0);

        this.run(
            // Input
            List.of(one),
            List.of(two),
            // Expected Output
            List.of(),
            0,
            List.of(List.of(
                List.of(two),
                List.of(one)
            )),
            List.of()
        );
    }

    @Test
    public void testReplace() throws IOException, InterruptedException {
        final var one = new MockService(0, 0, 0);
        final var twoA = new MockService(1, 0, 0);
        final var twoB = new MockService(1, 0, 1);

        this.run(
            // Input
            List.of(
                one,
                twoB
            ),
            List.of(
                one,
                twoA
            ),
            // Expected Output
            List.of(),
            0,
            List.of(
                List.of(
                    List.of(twoA),
                    List.of(twoB)
                )
            ),
            List.of()
        );
    }

    private void run(
        final List<Service> src, final List<Service> tar,
        final List<List<Service>> creates, final int read, final List<List<List<Service>>> updates, final List<List<Service>> deletes
    ) throws IOException, InterruptedException {
        final var source = Mockito.mock(ServiceProvider.class);
        Mockito.when(source.collectServiceNames()).thenReturn(src);

        final var target = Mockito.mock(ServiceStorage.class);
        Mockito.when(target.collectServiceNames()).thenReturn(tar);

        final var diff = new DiffUtilsPatcher();
        final var result = diff.apply(source, target);
        Assertions.assertEquals(creates.size(), result.getCreate(), "CREATE mismatch");
        Assertions.assertEquals(read, result.getRead(), "READ mismatch");
        Assertions.assertEquals(updates.size(), result.getUpdates(), "UPDATES mismatch");
        Assertions.assertEquals(deletes.size(), result.getDeletes(), "DELETES mismatch");

        Mockito.verify(source, Mockito.times(1)).collectServiceNames();
        Mockito.verify(target, Mockito.times(1)).collectServiceNames();

        for (final var create : creates) {
            Mockito.verify(target, Mockito.times(1)).insert(create);
        }

        for (final var update : updates) {
            Assertions.assertEquals(2, update.size());
            Mockito.verify(target, Mockito.times(1)).replace(
                update.get(0),
                update.get(1)
            );
        }
        for (final var delete : deletes) {
            Mockito.verify(target, Mockito.times(1)).delete(delete);
        }

        Mockito.verifyNoMoreInteractions(source, target);
    }
}
