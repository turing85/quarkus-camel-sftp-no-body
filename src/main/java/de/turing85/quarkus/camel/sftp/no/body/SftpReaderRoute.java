package de.turing85.quarkus.camel.sftp.no.body;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sftp;

@ApplicationScoped
@Slf4j
public class SftpReaderRoute extends RouteBuilder {
  public static final String SFTP_READER_ROUTE = "sftp-reader-route";

  @Override
  public void configure() {
    // @formatter:off
    from(
        sftp("localhost:2222/upload")
            .username("sftp-user")
            .password("sftp-pass")
            .delete(true)
            .streamDownload(true)
            .advanced()
                .stepwise(false))
        .routeId(SFTP_READER_ROUTE)
        .log("file name read: ${header.%s}".formatted(Exchange.FILE_NAME))
        .log("file content read (length: ${headers.%s}): ${body}".formatted(Exchange.FILE_LENGTH));
    // @formatter:on
  }
}
