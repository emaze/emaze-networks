package net.emaze.networks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.emaze.dysfunctional.Applications;
import net.emaze.dysfunctional.Compositions;
import net.emaze.dysfunctional.Consumers;
import net.emaze.dysfunctional.Multiplexing;
import net.emaze.dysfunctional.Tuples;
import net.emaze.dysfunctional.casts.Vary;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.dispatching.delegates.Delegate;
import net.emaze.dysfunctional.ranges.DenseRange;
import net.emaze.dysfunctional.ranges.Range;

public class DensifyCidrs implements Delegate<Set<Cidr>, Collection<Cidr>> {

    private static final Delegate<DenseRange<Ip>, Cidr> cidrToDenseRange = Compositions.compose(Tuples.tupled(new IpsToDenseRange()), new CidrToIps());
    private static final Delegate<List<Cidr>, DenseRange<Ip>> denseRangeToCidr = Compositions.compose(Tuples.tupled(new IpRangeToCidrs()), new DenseRangeToIps());

    @Override
    public Set<Cidr> perform(Collection<Cidr> cidrs) {
        dbc.precondition(cidrs != null, "cidrs cannot be null");
        final Iterator<Range<Ip>> childrenAsRanges = Applications.transform(cidrs, Compositions.compose(new Vary<Range<Ip>, DenseRange<Ip>>(), cidrToDenseRange));
        final Range<Ip> union = new Ipv4Ranges().union(childrenAsRanges);
        final List<DenseRange<Ip>> densifiedUnion = union.densified();
        return Consumers.all(Multiplexing.flatten(Applications.transform(densifiedUnion, denseRangeToCidr)), new HashSet<Cidr>());
    }
}
