package net.emaze.networks;

import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.equality.EqualsBuilder;
import net.emaze.dysfunctional.hashing.HashCodeBuilder;

public class Ip implements Comparable<Ip> {

    private static final int MIN_ADDRESS_SPACE = 0x00000000;
    private static final int MAX_ADDRESS_SPACE = 0xFFFFFFFF;
    public static final Ip LAST_IP = new Ip(MAX_ADDRESS_SPACE);
    public static final Ip FIRST_IP = new Ip(MIN_ADDRESS_SPACE);
    private final int address;

    private Ip(int bits) {
        this.address = bits;
    }

    public static Ip parse(String dottedIpAddress) {
        final long address = new DottedOctetFormToLong().perform(dottedIpAddress);
        return new Ip((int) address);
    }

    public static Ip fromBits(int ip) {
        return new Ip(ip);
    }

    public int toBits() {
        return address;
    }

    public Ip mask(Mask mask) {
        dbc.precondition(mask != null, "netmask cannot be null");
        return new Ip(address & mask.bits());
    }

    public Ip next() {
        return address == MAX_ADDRESS_SPACE ? LAST_IP : new Ip(address + 1);
    }

    public Ip previous() {
        return address == MIN_ADDRESS_SPACE ? FIRST_IP : new Ip(address - 1);
    }

    @Override
    public String toString() {
        return new IntToDottedOctetForm().perform(address);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Ip == false) {
            return false;
        }
        final Ip ipv4 = (Ip) other;
        return new EqualsBuilder().append(this.address, ipv4.address).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(address).toHashCode();
    }

    @Override
    public int compareTo(Ip other) {
        dbc.precondition(other != null, "other cannot be null");
        final int highestDifferentBit = Integer.highestOneBit(address ^ other.address);
        if (highestDifferentBit == 0) {
            return 0;
        }
        if ((address & highestDifferentBit) != 0) {
            return 1;
        }
        return -1;
    }
}
