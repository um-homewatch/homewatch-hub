package homewatch.things.services.thermostat;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.NetworkThingService;
import homewatch.things.NetworkThingServiceFactory;
import homewatch.things.ThingService;


public class ThermostatServiceFactory implements NetworkThingServiceFactory<Thermostat> {
  @Override
  public NetworkThingService<Thermostat> create(String address, Integer port, String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestThermostatService(address, port);
      default:
        throw new InvalidSubTypeException();
    }
  }

  @Override
  public ThingService<Thermostat> create(String subtype) throws InvalidSubTypeException {
    switch (subtype) {
      case "rest":
        return new RestThermostatService();
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
  REST
}