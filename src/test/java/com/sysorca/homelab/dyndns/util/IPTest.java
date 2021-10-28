package com.sysorca.homelab.dyndns.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public final class IPTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "1.1.1.1", "238.47.124.100", "7.252.75.49", "200.89.119.133",
            "131.24.93.227", "143.25.91.91", "68.140.137.43", "26.204.139.95",
            "152.222.215.96", "254.53.14.79", "228.183.175.164", "155.119.130.249",
            "94.106.189.56", "220.60.155.175", "133.46.68.99", "234.186.251.61",
            "79.117.136.115", "75.97.212.187", "28.21.134.212", "198.24.200.239",
            "162.240.117.75", "66.68.116.74", "112.14.67.44", "151.186.133.43",
    })
    public void detectsIPv4(final String address) throws IOException {
        final var ip = IP.create(address);
        Assertions.assertEquals(IPV.V4, ip.getVersion());
        Assertions.assertEquals(address, ip.getAddress());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2d70:d6b:5065:8d4e:9825:5d24:b87b:e0a3", "ea50:adae:5cf8:37a:596b:7d20:e048:b205",
            "4c3f:7776:7cef:5806:6afc:7693:dc24:73b6", "9ee0:4276:4bf1:51fd:5878:122:8f9a:44d",
            "b1be:d83f:4212:ec1e:e1ac:a7d8:2ed9:fe82", "25c1:1847:3141:4625:da7:10a8:753e:f4e5",
            "6299:6207:9d97:6181:2d9e:e04f:c7db:e2c", "9461:dd56:3735:bce5:d624:594f:d4d8:eb1a",
            "ca6a:9e8:8d40:a28e:af96:eaf5:530d:5f3a", "a0e1:752c:2932:8159:5f2e:f28c:bf33:e07f",
            "7b69:1853:5822:6e40:bbf4:621d:24bd:2e81", "a178:b07d:7f4b:5e6f:9b5:9e2:9a2:237e",
            "3985:d72d:c0fc:7cef:965a:a401:d99:943b", "ee41:4776:b99e:58c3:cd7e:80a2:cc3e:73fb",
            "c0b:157a:12b9:f5:d683:e433:3dff:a997", "3072:82fb:1c60:cbb1:8c4d:4d38:a0bb:1faa",
            "c846:4631:7ea1:7a3b:eb79:6948:a2e8:14cf", "5d30:9db8:726b:9353:2457:305b:84c:3b88",
            "1781:1380:cbcf:fd5f:5076:fa51:fc73:2ab2", "c2cd:1a96:13e2:ee98:354a:12a7:861e:7079",
            "c77e:6b87:2a91:bc94:f36c:59c:28ee:41ba", "9a75:9526:2cb5:2ab5:685c:6736:f6f0:8a7d",
            "975f:fef9:ef64:9ab8:7c78:4f83:1fbc:f365", "733d:6c8b:7ebd:3143:ca96:2e13:5bd0:eaa7"
    })
    public void detectsIPv6(final String address) throws IOException {
        final var ip = IP.create(address);
        Assertions.assertEquals(IPV.V6, ip.getVersion());
        Assertions.assertEquals(address, ip.getAddress());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "asdf", "invalid ip", "error", "{\"test\":\"Test\"}",
            "1.1.1.02", "::::", "1::", "444", "1111"
    })
    public void throwsExceptionsForInvalidIP(final String address) {
        final var ex = Assertions.assertThrows(IOException.class, () -> IP.create(address));
        Assertions.assertEquals("Could not detect IP Type", ex.getMessage());
    }
}
