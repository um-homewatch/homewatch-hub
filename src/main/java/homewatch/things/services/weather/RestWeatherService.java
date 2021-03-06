package homewatch.things.services.weather;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.exceptions.ReadOnlyDeviceException;
import homewatch.net.HttpUtils;
import homewatch.net.ThingResponse;
import homewatch.things.HttpThingService;

import java.io.IOException;

class RestWeatherService extends HttpThingService<Weather> {
  RestWeatherService() {
    super();
  }

  RestWeatherService(String ipAddress) {
    super(ipAddress);
  }

  RestWeatherService(String ipAddress, Integer port) {
    super(ipAddress, port);
  }

  public Weather get() throws NetworkException {
    try {
      ThingResponse response = HttpUtils.get(this.getBaseUrl());

      JsonNode jsonResponse = JsonUtils.getOM().readTree(response.getPayload());

      return jsonToWeather(jsonResponse);
    } catch (IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Weather put(Weather weather) throws NetworkException {
    throw new ReadOnlyDeviceException();
  }

  @Override
  public boolean ping() {
    try {
      return HttpUtils.get(getBaseUrl()).getStatusCode() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "Things::Weather";
  }

  @Override
  public String getSubtype() {
    return "rest";
  }

  private String getBaseUrl() {
    return this.getUrl() + "/status";
  }

  private Weather jsonToWeather(JsonNode json) {
    double temp = json.get("temperature").asDouble();
    double windSpeed = json.get("windspeed").asDouble();
    boolean raining = json.get("raining").asBoolean();
    boolean cloudy = json.get("cloudy").asBoolean();

    return new Weather(temp, windSpeed, raining, cloudy);
  }
}
