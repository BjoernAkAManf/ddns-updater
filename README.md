# Project FAISTY

A simple Java Programm that allows to update DNS settings on Storage (e.g. Cloudflare)
based on the desired state specified by an Provider (e.g. Docker labels). This is intended to be useful especially
within a home lab setting, where a proxy (e.g. Cloudflare) might protect the public IP of the private network. However,
it might also be useful in settings, where the operational overhead of Kubernetes is undesirable and DNS management
could be provided without too much overhead.

Right now this Project is still new and requires a lot of polish.

## Building

This project is build mainly through [earthly](https://earthly.dev). After installation, you should be able to run:
> earthly +main

Local development through maven is also possible, albeit not supported directly.

## Goals

* Add [TRAEFIK Proxy](https://traefik.io/traefik/) support ( Routing through the proxy, DNS managed through FAISTY )
* Add more Tests
* Add more documentation
* Integrate Github Actions to publish Docker Images
* Integrate Graal to reduce the required overhead of Java
* Add Configuration System to configure which Providers or Storage Systems to use
* Remain simple and easy to use

## License

[See LICENSE file](LICENSE)