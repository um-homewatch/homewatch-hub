package homewatch.things.weather;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.HttpThingService;
import homewatch.things.HttpThingServiceFactory;
import homewatch.things.ThingService;

import java.net.InetAddress;

public class WeatherServiceFactory implements HttpThingServiceFactory<Weather> {
  @Override
  public ThingService<Weather> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "owm":
        return new OWMWeatherService();
      case "rest":
        return new RestWeatherService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public HttpThingService<Weather> create(InetAddress address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestWeatherService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subTypeUpper = subtype.toUpperCase();

    try {
      SubType.valueOf(subTypeUpper);

      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}

enum SubType {
  REST, OWM
}