package homewatch.things;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.stubs.LightStubs;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import homewatch.things.services.lights.Light;
import homewatch.things.services.lights.LightServiceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NetworkThingDiscoveryService.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestDiscover {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  @Test
  public void discoverRestLights() throws Exception {
    LightStubs.stubGetRest(wireMockRule, true);

    NetworkThingDiscoveryService<Light> networkThingDiscoveryService = spy(new NetworkThingDiscoveryService<>(new LightServiceFactory(), "rest", 8080));

    doReturn(Collections.singletonList(InetAddress.getLocalHost().getHostAddress())).when(networkThingDiscoveryService, "getAddressList");

    List<ThingService<Light>> lights = networkThingDiscoveryService.perform();

    assertThat(lights.size(), is(1));
  }
}
