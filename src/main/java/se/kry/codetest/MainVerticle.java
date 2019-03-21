package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private HashMap<String, Service> services = new HashMap<>();
  //TODO use this
  private DBConnector connector;
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);
    Router router = Router.router(vertx);
    getServicesFromDB();
    router.route().handler(BodyHandler.create());
    Service kryService = new Service("kry","UNKNOWN");
    services.put("https://www.kry.se", kryService);
    vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(services));
    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/*").handler(StaticHandler.create());
    router.get("/service").handler(req -> {
      List<JsonObject> jsonServicesBis = services
              .entrySet()
              .stream()
              .map(service ->
                      new JsonObject()
                        .put("url", service.getKey())
                        .put("name", service.getValue().name)
                        .put("status", service.getValue().status)
                        .put("creationDate", service.getValue().creactionDate))
              .collect(Collectors.toList());
      req.response()
          .putHeader("content-type", "application/json")
          .end(new JsonArray(jsonServicesBis).encode());
    });
    router.post("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      updateServiceAndDB(jsonBody.getString("url"), jsonBody.getString("name"),"UNKNOWN", jsonBody.getBoolean("deleteService"));
      req.response()
          .putHeader("content-type", "text/plain")
          .end("OK");
    });
  }

  private void updateServiceAndDB(String url, String name, String status, boolean deleteService) {
    String query;
    JsonArray params = new JsonArray();
    if (deleteService) {
      services.remove(url);
      query = "DELETE FROM service WHERE url=?";
      params.add(url);
    } else {
      Service service = new Service(name, status);
      services.put(url,service);
      query = "INSERT INTO service (url, name, status, creationDate) VALUES (?,?,?,?)";
      params.add(url).add(name).add(status).add(service.creactionDate);
    }
    connector.query(query,params).setHandler(done -> {
      if(done.succeeded()){
        System.out.println("add successful");
      } else {
        done.cause().printStackTrace();
      }
    });
  }

  private void getServicesFromDB() {
    String getQuery = "SELECT * from service";
    connector.query(getQuery).setHandler(done -> {
      if(done.succeeded()){
        ResultSet result = done.result();
        List<JsonArray> urlServices = result.getResults();
        for (JsonArray urlService : urlServices) {
          Service service = new Service(urlService.getString(1),urlService.getString(2),urlService.getString(3));
          services.put(urlService.getString(0),service);
        }
        System.out.println("get * from service successful");
      } else {
        done.cause().printStackTrace();
      }
    });
  }

}



