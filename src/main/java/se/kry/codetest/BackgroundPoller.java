package se.kry.codetest;

import io.vertx.core.Future;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BackgroundPoller {

  public Future<List<String>> pollServices(HashMap<String, Service> services) {
    return Future.failedFuture("TODO");
  }
}
