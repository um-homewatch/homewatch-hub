package homewatch.things.lights;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.exceptions.NetworkException;
import homewatch.net.NetUtils;
import homewatch.things.HttpThingService;
import okhttp3.HttpUrl;
import org.json.JSONObject;

import java.net.InetAddress;

public class HueLightService extends HttpThingService<Light> {
  private int lightID;

  HueLightService() {
    super();
  }

  HueLightService(InetAddress ipAddress) {
    super(ipAddress);
  }

  HueLightService(InetAddress ipAddress, Integer port) {
    super(ipAddress, port);
  }

  public int getLightID() {
    return lightID;
  }

  public void setLightID(int lightID) {
    this.lightID = lightID;
  }

  @Override
  public Light get() throws NetworkException {
    HttpUrl url = HttpUrl.parse(String.format("%s/%d", this.baseUrl(), lightID));

    JsonNode response = NetUtils.get(url).getJson();

    return this.jsonToLight(response);
  }

  @Override
  public Light put(Light light) throws NetworkException {
    JSONObject json = new JSONObject();
    json.put("on", light.isOn());

    NetUtils.put(HttpUrl.parse(String.format("%s/%d/state", this.baseUrl(), lightID)), json);

    return light;
  }

  @Override
  public boolean ping() {
    try {
      HttpUrl url = HttpUrl.parse(String.format("%s/%d", this.baseUrl(), lightID));

      return NetUtils.get(url).getResponse().code() == 200;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public String getType() {
    return "lights";
  }

  @Override
  public String getSubType() {
    return "hue";
  }

  private HttpUrl baseUrl() {
    return HttpUrl.parse(this.getUrl() + "/api/newdeveloper/lights");
  }

  private Light jsonToLight(JsonNode json) {
    boolean on = json.get("state").get("on").asBoolean();

    return new Light(on);
  }
}