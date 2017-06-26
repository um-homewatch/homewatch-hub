package homewatch.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import homewatch.constants.JsonUtils;
import homewatch.constants.LoggerUtils;
import homewatch.exceptions.InvalidSubTypeException;
import homewatch.exceptions.NetworkException;
import org.xml.sax.SAXException;
import spark.Response;
import spark.Spark;

import java.io.IOException;

public class Main {
  private Main() {
  }

  public static void main(String[] args) throws IOException, SAXException {
    //set all responses to json format
    Spark.before((request, response) -> response.header("Content-Type", "application/json"));

    TokenSecurity.perform();

    Routes.perform();

    exceptionHandling();
  }

  private static void exceptionHandling() {
    Spark.exception(IllegalArgumentException.class, (exception, req, res) -> resolveException(exception, res, 400));

    Spark.exception(InvalidSubTypeException.class, (exception, req, res) -> resolveException(exception, res, 400));

    Spark.exception(NetworkException.class, (exception, req, res) -> {
      NetworkException networkException = (NetworkException) exception;
      resolveException(exception, res, networkException.getStatusCode());
    });

    Spark.exception(Exception.class, (exception, req, res) -> resolveException(exception, res, 500));
  }

  private static void resolveException(Exception exception, Response res, int statusCode) {
    res.header("Content-Type", "application/json");
    res.status(statusCode);
    res.body(exceptionToString(exception));
    LoggerUtils.logException(exception);
  }

  private static String exceptionToString(Exception e) {
    try {
      return JsonUtils.getOM().writeValueAsString(new ErrorMessage(e));
    } catch (JsonProcessingException e1) {
      LoggerUtils.logException(e1);
      return null;
    }
  }
}
