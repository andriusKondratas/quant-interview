package org.example.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.persistence.entity.NetworkConnection;
import org.example.persistence.projections.TopDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NetworkConnectionRepository extends JpaRepository<NetworkConnection, Long> {

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query(value =
      """
          delete {h-schema}network_connection c 
                           where nvl(datediff(minute, c.timestamp, current_timestamp),0) > ?1
          """,
      nativeQuery = true)
  int deleteOutDatedNetworkConnections(long dataRetentionInMinutes);

  @Query(
      value =
          """
                  select
                    c.domain,
                    count(distinct c.blueprint) connectionCount              
                   from {h-schema}network_connection c
                    where c.timestamp > ?1
                   group by c.domain
                   order by count(distinct c.blueprint) desc offset 0 rows fetch next ?2 rows only
              """,
      nativeQuery = true)
  List<TopDomain> findTopDomains(
      LocalDateTime lastCorrelatedTimestamp,
      Integer topCount
  );

}
