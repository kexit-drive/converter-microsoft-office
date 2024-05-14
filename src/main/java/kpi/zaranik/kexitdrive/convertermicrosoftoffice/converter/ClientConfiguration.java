package kpi.zaranik.kexitdrive.convertermicrosoftoffice.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class ClientConfiguration {

    private static final int MAX_IN_MEMORY_SIZE_IN_BYTES = Math.toIntExact(DataSize.ofMegabytes(512).toBytes());

    @Bean
    public WebClient webClient() {
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE_IN_BYTES))
            .build();
        return WebClient.builder()
            .baseUrl("http://localhost:3000")
            .exchangeStrategies(strategies)
            .build();
    }

}
