package org.example.monitor.component.executor;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.example.common.component.Executable;
import org.example.common.service.CounterStore;
import org.example.monitor.component.CsvToNetworkConnectionDtoReader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CsvFileParseExecutor implements Executable {

  final CsvToNetworkConnectionDtoReader csvToConnectionDtoReader;
  final CounterStore counterStore;

  @Override
  public boolean execute(Path directory, Path file) {

    var v = csvToConnectionDtoReader.read(file);
    counterStore.sinkConnections(v);

    return true;
  }
}
