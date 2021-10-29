package com.sysorca.homelab.dyndns.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class CRUDCounterTester {
    @Test
    public void createIncrements() {
        final var m = new CRUDCounter();
        Assertions.assertEquals(0, m.getCreate());
        m.create();
        Assertions.assertEquals(1, m.getCreate());
        m.create();
        m.create();
        Assertions.assertEquals(3, m.getCreate());
    }

    @Test
    public void readIncrements() {
        final var m = new CRUDCounter();
        Assertions.assertEquals(0, m.getRead());
        m.read();
        Assertions.assertEquals(1, m.getRead());
        m.read();
        m.read();
        Assertions.assertEquals(3, m.getRead());
    }

    @Test
    public void updateIncrements() {
        final var m = new CRUDCounter();
        Assertions.assertEquals(0, m.getUpdates());
        m.update();
        Assertions.assertEquals(1, m.getUpdates());
        m.update();
        m.update();
        Assertions.assertEquals(3, m.getUpdates());
    }

    @Test
    public void deleteIncrements() {
        final var m = new CRUDCounter();
        Assertions.assertEquals(0, m.getDeletes());
        m.delete();
        Assertions.assertEquals(1, m.getDeletes());
        m.delete();
        m.delete();
        Assertions.assertEquals(3, m.getDeletes());
    }
}
