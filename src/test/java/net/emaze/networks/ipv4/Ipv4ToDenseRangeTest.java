package net.emaze.networks.ipv4;

import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.ranges.DenseRange;
import org.junit.Assert;
import org.junit.Test;

public class Ipv4ToDenseRangeTest {

    @Test
    public void lastElementOfIterationIsLastIp() {
        final Ipv4 firstIp = Ipv4.parse("127.0.0.0");
        final Ipv4 lastIp = Ipv4.parse("127.0.0.1");
        final DenseRange<Ipv4> allSpace = new Ipv4ToDenseRange().apply(firstIp, lastIp);
        Assert.assertEquals(true, (allSpace.begin().equals(firstIp)) && (Consumers.last(allSpace).equals(lastIp)));
    }
}
