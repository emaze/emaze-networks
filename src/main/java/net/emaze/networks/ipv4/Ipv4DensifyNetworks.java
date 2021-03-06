package net.emaze.networks.ipv4;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.emaze.dysfunctional.Applications;
import net.emaze.dysfunctional.Compositions;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.dysfunctional.Tuples;
import net.emaze.dysfunctional.casts.Vary;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.ranges.DenseRange;
import net.emaze.dysfunctional.ranges.Range;
import net.emaze.networks.IpRanges;

public class Ipv4DensifyNetworks implements Function<Collection<Ipv4Network>, Set<Ipv4Network>> {

    private final Function<Ipv4Network, DenseRange<Ipv4>> cidrToDenseRange = Compositions.compose(Tuples.tupled(new Ipv4ToDenseRange()), new Ipv4NetworkToIp());
    private final Function<DenseRange<Ipv4>, List<Ipv4Network>> denseRangeToCidr = Compositions.compose(Tuples.tupled(new Ipv4RangeToNetworks()), new Ipv4DenseRangeToIp());

    @Override
    public Set<Ipv4Network> apply(Collection<Ipv4Network> cidrs) {
        dbc.precondition(cidrs != null, "cidrs cannot be null");
        final Iterator<Range<Ipv4>> childrenAsRanges = Applications.transform(cidrs, Compositions.compose(new Vary<DenseRange<Ipv4>, Range<Ipv4>>(), cidrToDenseRange));
        final Range<Ipv4> union = IpRanges.RANGESV4.union(childrenAsRanges);
        final List<DenseRange<Ipv4>> densifiedUnion = union.densified();
        return Consumers.all(Multiplexing.flatten(Applications.transform(densifiedUnion, denseRangeToCidr)), new HashSet<Ipv4Network>());
    }

}
