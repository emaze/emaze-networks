package net.emaze.networks.ipv4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.order.CompareToBuilder;

public class Ipv4SortNetworksByFirstThenLastIp implements Function<Collection<Ipv4Network>, List<Ipv4Network>> {

    @Override
    public List<Ipv4Network> apply(Collection<Ipv4Network> unsorted) {
        dbc.precondition(unsorted != null, "unsorted cannot be null");
        final List<Ipv4Network> sorted = new ArrayList<>(unsorted);
        Collections.sort(sorted, new FirstIpThenLastIpCidrComparator());
        return sorted;
    }

    private static class FirstIpThenLastIpCidrComparator implements Comparator<Ipv4Network> {

        @Override
        public int compare(Ipv4Network lhs, Ipv4Network rhs) {
            return new CompareToBuilder().append(lhs.firstIp(), rhs.firstIp()).append(lhs.lastIp(), rhs.lastIp()).toComparison();
        }
    }
}
