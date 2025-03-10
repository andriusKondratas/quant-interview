package org.example.persistence.projections;

import org.example.common.model.TopDomainDto;

public interface TopDomain {

  String getDomain();

  Integer getConnectionCount();

  static TopDomainDto toDto(TopDomain topDomain) {
    var topDomainDto = new TopDomainDto();

    topDomainDto.setDomain(topDomain.getDomain());
    topDomainDto.setConnectionCount(topDomain.getConnectionCount().toString());

    return topDomainDto;
  }
}
