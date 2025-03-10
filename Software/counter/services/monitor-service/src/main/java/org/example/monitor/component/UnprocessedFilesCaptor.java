package org.example.monitor.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnprocessedFilesCaptor {

  public Set<Path> captureUnprocessedFiles(Path sourcePath, int fileBatchLimit) {
    return captureFiles(sourcePath, fileBatchLimit, "failed", "processed");
  }

  public static Set<Path> captureFiles(Path capturePath, int limit,
      String... excludeContaining) {
    log.debug("Reading files from directory: [{}]", capturePath.toFile().getAbsolutePath());
    Set<Path> result = new HashSet<>();

    Predicates predicates = new Predicates(new HashSet<>(Arrays.asList(excludeContaining)), Set.of(".csv"));

    Comparator<Path> fileNameComparator
        = Comparator.comparing(Path::getFileName);
    Comparator<Path> fileNameComparatorReversed
        = fileNameComparator.reversed();

    try {
      try (Stream<Path> files = Files.list(capturePath)) {
        result = files.filter(predicates::noneMatch)
            .filter(predicates::anyMatch)
            .filter(predicates::fileNameIsValid)
            .filter(predicates::fileIsFinal)
            .sorted(fileNameComparatorReversed)
            .limit(limit)
            .collect(Collectors.toCollection(HashSet::new));
      }
    } catch (IOException e) {
      log.error("Specified directory {} does not exist. Check environment variables", capturePath);
    }
    return result;
  }

  static class Predicates {

    private final Set<String> unmatchable;
    private final Set<String> matchable;

    Predicates(Set<String> unmatchable, Set<String> matchable) {
      this.unmatchable = unmatchable;
      this.matchable = matchable;
    }

    boolean noneMatch(Path file) {
      return unmatchable.stream()
          .noneMatch(s -> file.getFileName().toString().toUpperCase().contains(s.toUpperCase()));
    }

    boolean anyMatch(Path file) {
      return matchable.stream()
          .anyMatch(s -> file.getFileName().toString().toUpperCase().contains(s.toUpperCase()));
    }

    boolean fileNameIsValid(Path file) {
      //TODO
      return true;
    }

    boolean fileIsFinal(Path file) {
      //TODO
      return true;
    }
  }
}