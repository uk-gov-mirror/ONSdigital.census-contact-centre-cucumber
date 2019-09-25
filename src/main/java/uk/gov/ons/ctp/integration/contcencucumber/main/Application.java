package uk.gov.ons.ctp.integration.contcencucumber.main;

import java.util.HashMap;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventSender;
import uk.gov.ons.ctp.common.event.SpringRabbitEventSender;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.rest.RestClientConfig;
// import uk.gov.ons.ctp.common.rest.RestClient;
// import uk.gov.ons.ctp.common.rest.RestClientConfig;
import uk.gov.ons.ctp.integration.contcencucumber.config.AppConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.IntegrationComponentScan;
import uk.gov.ons.ctp.common.event.EventPublisher;

//@ImportResource("springintegration/main.xml")
//@SpringBootApplication
//@IntegrationComponentScan("uk.gov.ons.ctp.integration")
//@ComponentScan(basePackages = {"uk.gov.ons.ctp.integration"})
//@EnableCaching
public class Application {

  private AppConfig appConfig;
  
  @Value("${queueconfig.event-exchange}")
  private String eventExchange;

  private static final HashMap<HttpStatus, HttpStatus> httpErrorMapping;

  static {
    httpErrorMapping = new HashMap<HttpStatus, HttpStatus>();
    httpErrorMapping.put(HttpStatus.OK, HttpStatus.OK);
    httpErrorMapping.put(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorMapping.put(HttpStatus.UNAUTHORIZED, HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorMapping.put(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
    httpErrorMapping.put(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorMapping.put(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.INTERNAL_SERVER_ERROR);
    httpErrorMapping.put(HttpStatus.REQUEST_TIMEOUT, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // This is the http status to be used for error mapping if a status is not in the mapping table
  HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  /**
   * Constructor for Application
   *
   * @param appConfig contains the configuration for the current deployment.
   */
  @Autowired
  public Application(final AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Bean
  @Qualifier("eventGeneratorClient")
  public RestClient eventGeneratorClient() {
    RestClientConfig clientConfig = appConfig.getEventGeneratorSettings().getRestClientConfig();
    RestClient restHelper = new RestClient(clientConfig, httpErrorMapping, defaultHttpStatus);
    return restHelper;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
  /**
   * Bean used to publish asynchronous event messages
   *
   * @param connectionFactory RabbitMQ connection settings and strategies
   * @return the event publisher
   */
  @Bean
  public EventPublisher eventPublisher(final ConnectionFactory connectionFactory) {
    final var template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(new Jackson2JsonMessageConverter());
    template.setExchange(eventExchange);
    template.setChannelTransacted(true);

    EventSender sender = new SpringRabbitEventSender(template);
    return new EventPublisher(sender);
  }
}
