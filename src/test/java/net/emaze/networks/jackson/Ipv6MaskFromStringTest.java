package net.emaze.networks.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.networks.ipv6.Ipv6Mask;
import org.junit.Assert;
import org.junit.Test;

public class Ipv6MaskFromStringTest {

    @Test
    public void deserializingIPv6MaskYieldsExpected() throws IOException {
        final String serialized = "{'netmask':115}".replace("'", "\"");
        final Ipv6Mask expected = Ipv6Mask.net(115);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new NetworksModule());
        final BeanWithIpv6Mask got = mapper.readValue(serialized, BeanWithIpv6Mask.class);
        Assert.assertEquals(expected, got.getNetmask());
    }
}
