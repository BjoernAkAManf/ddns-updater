package com.sysorca.homelab.dyndns.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.sysorca.homelab.dyndns.Service;
import com.sysorca.homelab.dyndns.ServiceProvider;
import com.sysorca.homelab.dyndns.util.IP;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public final class DockerServices implements ServiceProvider {
    private static final String INGRESS_LABEL_HOST = "ingress.sysorca.com/host";
    private static final String INGRESS_LABEL_NAME = "ingress.sysorca.com/name";
    private final DockerClient client;
    private final IP address;

    public DockerServices(final DockerClientConfig config, final IP address) {
        final var httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        this.client = DockerClientImpl.getInstance(config, httpClient);
        this.address = address;
    }

    @Override
    public List<Service> collectServiceNames() {
        log.info("- Start - ");
        final var m = this.client
                .listContainersCmd()
                .withLabelFilter(List.of(INGRESS_LABEL_HOST))
                .exec()
                .stream()
                .map(i -> {
                    final var labels = i.getLabels();
                    return (Service) new DockerService(
                            labels.getOrDefault(INGRESS_LABEL_NAME, getName(i)),
                            labels.get(INGRESS_LABEL_HOST),
                            this.address
                    );
                })
                .collect(Collectors.toList());
        log.info("- End -");
        return m;
    }

    private String getName(final Container container) {
        final var names = container.getNames();
        if (names.length > 0) {
            return names[0];
        }
        return container.getId();
    }
}
