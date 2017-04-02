package homewatch.things.weather;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.junit.Assert.assertTrue;

public class TestWeatherServiceFactory {
  private static final WeatherServiceFactory serviceFactory = new WeatherServiceFactory();

  @Test
  public void testOwmCreate() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.create("owm");

    assertTrue(weatherService instanceof OWMWeatherService);
  }

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Weather> weatherService = serviceFactory.create("rest");

    assertTrue(weatherService instanceof RestWeatherService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("owm"));
  }
}
