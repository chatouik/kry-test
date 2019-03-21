package se.kry.codetest;

import io.vertx.core.Future;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BackgroundPoller {

  public Future<List<String>> pollServices(HashMap<String, Service> services) {
    String a = "ree";
    System.out.println("i am in poller");
    return Future.failedFuture("TODO");
  }
}
