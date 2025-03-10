package org.example.common.service;

import java.util.Map;
import org.example.common.component.Executable;

public interface ExecutorService {

  void execute();

  Map<Integer, Executable> getExecutors();

}
