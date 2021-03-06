package homewatch.things.discovery;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.Unirest;
import homewatch.server.ServerRunner;
import homewatch.stubs.LightStubs;
import homewatch.stubs.LockStubs;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NetworkInterfaceUtils.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestNetworkDiscoveryController extends ServerRunner {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private List<String> addresses;

  @Before
  public void setup() throws UnknownHostException, SocketException {
    addresses = Collections.singletonList(InetAddress.getLocalHost().getHostName());

    PowerMockito.mockStatic(NetworkInterfaceUtils.class);
    PowerMockito.when(NetworkInterfaceUtils.getAddressesInNetwork()).thenReturn(addresses);
  }

  @Test
  public void discoverRestLights() throws Exception {
    LightStubs.stubGetRest(wireMockRule, true);

    JSONObject json = Unirest.get("http://localhost:4567/devices/lights/discover")
        .queryString("subtype", "rest")
        .queryString("port", 8080)
        .asJson().getBody().getArray().getJSONObject(0);

    assertThat(json.get("address"), is(addresses.get(0)));
  }

  @Test
  public void testCode400() throws Exception {
    LockStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/devices/lights/discover")
        .queryString("subtype", "foo")
        .asJson()
        .getStatus();

    assertThat(status, is(400));
  }

  @Test
  public void testMissingParam() throws Exception {
    LockStubs.stubGetStatus(wireMockRule, true);

    int status = Unirest.get("http://localhost:4567/devices/lights/discover")
        .asJson()
        .getStatus();

    assertThat(status, is(400));
  }
}
