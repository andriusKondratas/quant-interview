package org.example.persistence;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.common.model.NetworkConnectionDto;
import org.example.common.model.TopDomainDto;
import org.example.common.service.CounterStore;
import org.example.persistence.entity.NetworkConnection;
import org.example.persistence.projections.TopDomain;
import org.example.persistence.repository.NetworkConnectionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterJDBCStore implements CounterStore {

  private final NetworkConnectionRepository networkConnectionRepository;

  @Override
  public void sinkConnections(List<NetworkConnectionDto> networkConnectionDtoList) {
    var networkConnections = networkConnectionDtoList.stream().map(NetworkConnection::fromDto).toList();

    networkConnectionRepository.saveAll(networkConnections);
  }

  @Override
  public List<NetworkConnectionDto> findAllNetworkConnections() {
    var networkConnections = networkConnectionRepository.findAll();

    return networkConnections.stream().map(NetworkConnection::toDto).toList();
  }

  @Override
  public List<TopDomainDto> findTopDomains(LocalDateTime lastCorrelatedTimestamp,
      Integer topCount) {
    var topDomains = networkConnectionRepository.findTopDomains(lastCorrelatedTimestamp,
        topCount);

    return topDomains.stream().map(TopDomain::toDto).toList();
  }

  @Override
  public long cleanNetworkConnections(Long retentionInMinutes) {
    return networkConnectionRepository.deleteOutDatedNetworkConnections(retentionInMinutes);
  }
}
