package homewatch.things.weather;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;

import java.net.InetAddress;

public class RestWeatherService extends HttpThingService<Weather> {
  RestWeatherService() {
    super();
  }

  RestWeatherService(InetAddress ipAddress) {
    super(ipAddress);
  }

  RestWeatherService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  @Override
  public Weather get() throws NetworkException {
    JsonNode response = NetUtils.get(getBaseUrl()).getJson();

    return this.jsonToWeather(response);
  }

  @Override
  public Weather put(Weather weather) throws NetworkException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean ping() {
    try {
      return NetUtils.get(getBaseUrl()).getResponse().code() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "weather";
  }

  @Override
  public String getSubType() {
    return "rest";
  }

  private HttpUrl getBaseUrl() {
    return HttpUrl.parse(this.getUrl() + "/status");
  }

  private Weather jsonToWeather(JsonNode json) {
    double temp = json.get("temperature").asDouble();
    double windSpeed = json.get("windspeed").asDouble();
    boolean raining = json.get("raining").asBoolean();
    boolean cloudy = json.get("cloudy").asBoolean();

    return new Weather(temp, windSpeed, raining, cloudy);
  }
}