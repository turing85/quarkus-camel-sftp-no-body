package de.turing85.quarkus.camel.sftp.no.body;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.scheduler;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sftp;

@ApplicationScoped
@Slf4j
public class SchedulerRoute extends RouteBuilder {
  private static final String SCHEDULER_ROUTE = "scheduler-route";

  @Override
  public void configure() {
    // @formatter:off
    from(
        scheduler(SCHEDULER_ROUTE)
            .delay(Duration.ofDays(1).toMillis())
            .initialDelay(Duration.ofSeconds(2).toMillis()))
        .routeId(SCHEDULER_ROUTE)
        .process(exchange -> {
          exchange.getIn().setBody(RandomStringUtils.secure().nextAlphanumeric(10));
          exchange.getIn().setHeader(Exchange.FILE_NAME, RandomStringUtils.secure().nextAlphabetic(5) + ".fips");
        })
        .to(sftp("localhost:2222/upload")
            .username("sftp-user")
            .password("sftp-pass")
            .useUserKnownHostsFile(false)
            .knownHosts("RAW([localhost]:2222 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCo36rKRRDgvI3v+xDgp3JTEomrTwn4pt3xwBCV6iTZypXNx00WguYG2Bq8AddXPa2nXjUllmRzjvT+LnzM7X6IB7pT4G9QBeVnXPSb/2OY+8oiubxF1yT710mg8mEPiJNWia1jwjhYjDpo7/Yw5qQzRdSvkIGKwb1v2bywOywko/xGB+1SANb2tGq44KRwylNHxGcLNjFVxMCLcXJvCN6H0GhZW9Sf5z0PRvzH0Y5rP2zJ/y1666o5pYmUMuLEuhiTyb/sJD0VJC4h8y/CPzdh0/7tlyuEcph4XT6yz9aWVikqDdXghjPa7uoNJVlTta/5O5rB1SFou2AususAIEUz6QfQqn1agr0zkfGxDb/wWDL40xpekvtOJCFlWTsENFrCK5uNKETVW6x7JcnlbSp4pGS9dTHRDq4hedJr1OmbqXfzMR8bioaUmJLspfu/VXB+wB1+BsvohWTNOUpImcbFT2wWL4GS00c6CrkemDOXYNCO9EQ/+pU2CGz/80KAmkU= marco@ecco)")
            .serverHostKeys("ssh-rsa")
            .strictHostKeyChecking("yes"));
    // @formatter:on
  }
}
