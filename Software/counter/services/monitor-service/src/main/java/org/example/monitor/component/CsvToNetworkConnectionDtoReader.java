package org.example.monitor.component;

import com.opencsv.CSVParser;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.example.common.model.NetworkConnectionDto;
import org.springframework.stereotype.Component;

@Component
public class CsvToNetworkConnectionDtoReader {

  public List<NetworkConnectionDto> read(Path inputCsvFile){

    List<NetworkConnectionDto> packetDtoList;
    try (Reader reader = Files.newBufferedReader(inputCsvFile)) {
      CsvToBean<NetworkConnectionDto> cb = new CsvToBeanBuilder<NetworkConnectionDto>(reader)
          .withType(NetworkConnectionDto.class)
          .withIgnoreLeadingWhiteSpace(true)
          .withSeparator(CSVParser.DEFAULT_SEPARATOR)
          .build();

      packetDtoList = cb.parse();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return packetDtoList;
  }
}
