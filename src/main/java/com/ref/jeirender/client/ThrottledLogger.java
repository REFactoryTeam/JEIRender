package com.ref.jeirender.client;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThrottledLogger {
  private final Logger logger;
  private final Set<String> buffer = new LinkedHashSet<>();

  public ThrottledLogger(String name) {
    this.logger = LogManager.getLogger(name);
  }

  public void log(String entry) {
    synchronized (buffer) {
      buffer.add(entry);
    }
  }

  public void flush() {
    synchronized (buffer) {
      if (!buffer.isEmpty()) {
        String output = String.join(", ", buffer);
        logger.info("Captured UIDs: [{}]", output);
        buffer.clear();
      }
    }
  }

  public void clear() {
    synchronized (buffer) {
      buffer.clear();
    }
  }
}
