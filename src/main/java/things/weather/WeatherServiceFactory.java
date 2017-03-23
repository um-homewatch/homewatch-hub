package things.weather;

import exceptions.InvalidSubTypeException;
import things.ThingService;
import things.ThingServiceFactory;

public class WeatherServiceFactory implements ThingServiceFactory<Weather> {
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
  public boolean isSubType(String subtype) {
    String subTypeUpper = subtype.toUpperCase();

    try {
      SubType.valueOf(subTypeUpper);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}

enum SubType {
  REST, OWM
}
