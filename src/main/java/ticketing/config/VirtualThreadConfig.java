package ticketing.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadConfig {

    @Bean
    public Executor taskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatVirtualThreads() {
        return factory -> factory.addConnectorCustomizers((Connector connector) ->
                connector.getProtocolHandler().setExecutor(Executors.newVirtualThreadPerTaskExecutor()));
    }
}
