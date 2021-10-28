package com.sysorca.homelab.dyndns;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.sysorca.homelab.dyndns.cloudflare.CloudflareServices;
import com.sysorca.homelab.dyndns.cloudflare.api.Request;
import com.sysorca.homelab.dyndns.diff.DiffUtilsPatcher;
import com.sysorca.homelab.dyndns.docker.DockerServices;
import com.sysorca.homelab.dyndns.ip.FallbackFetcher;
import com.sysorca.homelab.dyndns.ip.HttpFetcher;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

@Slf4j
public final class Main {
    private static FallbackFetcher retrieveIP(final HttpClient client) {
        return new FallbackFetcher(
                new HttpFetcher(client, URI.create("https://icanhazip.com/")),
                new HttpFetcher(client, URI.create("https://api64.ipify.org"))
        );
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final var client = HttpClient.newBuilder()
                .build();

        // TODO: Find public IP
        final var ipFetcher = retrieveIP(client);
        final var ip = ipFetcher.retrieveIP();

        // TODO: Find all required DNS from running Docker containers
        final var docker = new DockerServices(
                DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .build(),
                ip
        );

        // TODO: Find all currently existing DNS from Cloudflare
        // TODO: make sure all DNS records are proxied
        final var cloudflare = new CloudflareServices(
                new Request(client, System.getenv("CF_TOKEN")),
                System.getenv("CF_ZONE")
        );

        final var patcher = new DiffUtilsPatcher();

        final var changes = patcher.apply(docker, cloudflare);
        final var inserts = changes.getCreate();
        final var updates = changes.getUpdates();
        final var delete = changes.getDeletes();
        if (inserts == 0 && updates == 0 && delete == 0) {
            log.info("Already up to date!");
        } else {
            log.info("{} inserts, {} updates, {} deletes applied!", inserts, updates, delete);
        }

        // TODO: query all docker endpoints from portainer and query each for information ("federated docker")
    }
}
