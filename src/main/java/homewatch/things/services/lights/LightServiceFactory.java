package homewatch.things.services.lights;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.ThingService;


public class LightServiceFactory implements NetworkThingServiceFactory<Light> {
  @Override
  public NetworkThingService<Light> create(String address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "hue":
        return new HueLightService(address, port);
      case "rest":
        return new RestLightService(address, port);
      case "coap":
        return new CoapLightService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public ThingService<Light> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "hue":
        return new HueLightService();
      case "rest":
        return new RestLightService();
      case "coap":
        return new CoapLightService();
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public boolean isSubType(String subtype) {
    String subtypeUpper = subtype.toUpperCase();
    try {
      SubType.valueOf(subtypeUpper);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}

enum SubType {
  REST, HUE, COAP
}