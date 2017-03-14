package locks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import locks.LockStubs;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.xml.sax.SAXException;
import server.Main;
import things.exceptions.NetworkException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by joses on 13/03/2017.
 */
public class TestLockController {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  // Query string to send on each test
  private static Map<String, Object> QUERY_STRING;
  // JsonObject to change the thing status
  private static JSONObject JSON;

  @BeforeClass
  public static void setup() throws IOException, SAXException {
    QUERY_STRING = new HashMap<>();
    QUERY_STRING.put("address", "localhost");
    QUERY_STRING.put("port", 8080);
    QUERY_STRING.put("subType", "rest");

    JSON = new JSONObject();
    JSON.put("status", false);

    //startup the server
    Main.main(new String[1]);
  }

  @Test
  public void getStatus() throws UnknownHostException, NetworkException, UnirestException {
    LockStubs.stubGetStatus(wireMockRule, true);

    boolean status = Unirest.get("http://localhost:4567/locks").queryString(QUERY_STRING)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("locked");

    assertThat(status, is(true));
  }

  @Test
  public void setStatus() throws UnknownHostException, NetworkException, UnirestException {
    LockStubs.stubPutStatus(wireMockRule, false);
    LockStubs.stubGetStatus(wireMockRule, false);

    boolean status = Unirest.put("http://localhost:4567/locks").queryString(QUERY_STRING).body(JSON)
            .asJson()
            .getBody()
            .getObject()
            .getBoolean("locked");

    assertThat(status, is(false));
  }

  @Test
  public void errorInvalidArgument() throws UnirestException {
    LockStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/locks")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void errorInvalidSubType() throws UnirestException {
    LockStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/locks")
            .queryString("address", "192.168.1.1")
            .queryString("subType", "cenas")
            .asJson()
            .getStatus();

    assertThat(status, is(400));
  }
}
