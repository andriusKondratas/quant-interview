package org.example.common.model;

import lombok.Data;

@Data
public class TopDomainDto {

  String domain;
  String connectionCount;

  @Override
  public String toString() {
    return domain.concat(" - ")
        .concat(connectionCount)
        .concat(" connections");
  }
}
