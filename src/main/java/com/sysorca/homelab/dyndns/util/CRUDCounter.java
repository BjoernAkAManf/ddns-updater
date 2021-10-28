package com.sysorca.homelab.dyndns.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class CRUDCounter {
    private int create;
    private int read;
    private int updates;
    private int deletes;

    public void create() {
        this.create += 1;
    }

    public void read() {
        this.read += 1;
    }

    public void update() {
        this.updates += 1;
    }

    public void delete() {
        this.deletes += 1;
    }
}

