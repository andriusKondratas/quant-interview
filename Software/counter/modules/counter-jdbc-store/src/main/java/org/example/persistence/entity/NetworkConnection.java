package org.example.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.Data;
import org.example.common.model.NetworkConnectionDto;
import org.springframework.util.DigestUtils;


@Data
@Entity
@Table(
    name = "network_connection",
    indexes = {
        @Index(name = "domain-idx", columnList = "domain")
    })
public class NetworkConnection {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  @Column(name = "blueprint")
  private String blueprint;

  @Column(name = "domain")
  private String domain;


  public static NetworkConnection fromDto(NetworkConnectionDto networkConnectionDto) {
    var networkConnection = new NetworkConnection();

    networkConnection.setBlueprint(DigestUtils.md5DigestAsHex(networkConnectionDto.getBlueprint().getBytes()));

    Instant instant = Instant.ofEpochMilli(networkConnectionDto.getTimestamp());
    networkConnection.setTimestamp(LocalDateTime.ofInstant(instant, ZoneId.of("UTC")));
    networkConnection.setDomain(networkConnectionDto.getDomain());
    return networkConnection;
  }

  public static NetworkConnectionDto toDto(NetworkConnection networkConnection) {
    var networkConnectionDto = new NetworkConnectionDto();

    networkConnectionDto.setTimestamp(networkConnection.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli());
    networkConnectionDto.setDomain(networkConnection.getDomain());

    var t = DigestUtils.md5Digest(networkConnection.blueprint.getBytes());
    return networkConnectionDto;
  }
}
