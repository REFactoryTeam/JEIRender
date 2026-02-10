package com.ref.jeirender.client;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThrottledLogger {
  private final Logger logger;
  private final Set<String> buffer = new LinkedHashSet<>();
  private final long interval;
  private long nextLogTime;

  public ThrottledLogger(String name, long intervalMs) {
    this.logger = LogManager.getLogger(name);
    this.interval = intervalMs;
    this.nextLogTime = 0;
  }

  public void log(String entry) {
    synchronized (buffer) {
      buffer.add(entry);

      long currentTime = System.currentTimeMillis();
      if (currentTime >= nextLogTime) {
        flush();
        nextLogTime = currentTime + interval;
      }
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
