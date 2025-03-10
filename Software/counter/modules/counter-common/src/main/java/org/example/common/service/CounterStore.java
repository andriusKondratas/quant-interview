package org.example.common.service;

import java.time.LocalDateTime;
import java.util.List;
import org.example.common.model.NetworkConnectionDto;
import org.example.common.model.TopDomainDto;

public interface CounterStore {

  void sinkConnections(List<NetworkConnectionDto> networkConnections);

  long cleanNetworkConnections(Long retentionInMinutes);

  List<NetworkConnectionDto> findAllNetworkConnections();

  List<TopDomainDto> findTopDomains(LocalDateTime lastCorrelatedTimestamp,
      Integer topCount);

}
