package org.example.repo2cloud.config;

import org.example.repo2cloud.client.BuildServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Configures the {@link RestClient} that talks to the Build Server
 * and wires it into an {@link HttpServiceProxyFactory}-generated proxy
 * that implements {@link BuildServerClient}.
 *
 * <h3>How it works</h3>
 * <ol>
 *   <li>{@link #buildServerRestClient} creates a {@link RestClient} pointed at the Build Server URL
 *       with connection and read timeouts.</li>
 *   <li>{@link #buildServerClient} wraps that {@code RestClient} in a
 *       {@link RestClientAdapter}, feeds it to {@link HttpServiceProxyFactory},
 *       and produces a dynamic proxy of the {@link BuildServerClient} interface.</li>
 * </ol>
 *
 * <p>The Build Server URL is read from {@code repo2cloud.build-server.url} in
 * {@code application.properties} so it can be changed without modifying Java code.</p>
 */
@Configuration
public class BuildServerConfig {

    @Value("${repo2cloud.build-server.url:http://localhost:9090}")
    private String buildServerUrl;

    /**
     * Low-level {@link RestClient} configured with the Build Server base URL
     * and reasonable HTTP timeouts.
     *
     * <p>Connect timeout: 5 s — enough for local/network connections.<br>
     * Read timeout: 30 s — {@code POST /deploy} only waits for acceptance,
     * not for the full deployment pipeline.</p>
     */
    @Bean
    public RestClient buildServerRestClient(RestClient.Builder builder) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

        return builder
                .baseUrl(buildServerUrl)
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * Dynamic proxy implementing {@link BuildServerClient}.
     *
     * <p>{@link HttpServiceProxyFactory} reads the {@code @GetExchange},
     * {@code @PostExchange}, {@code @DeleteExchange} annotations on the
     * interface and generates an implementation that delegates every call
     * to the {@link RestClient} via {@link RestClientAdapter}.</p>
     */
    @Bean
    public BuildServerClient buildServerClient(RestClient buildServerRestClient) {
        RestClientAdapter adapter =
                RestClientAdapter.create(buildServerRestClient);

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BuildServerClient.class);
    }
}
