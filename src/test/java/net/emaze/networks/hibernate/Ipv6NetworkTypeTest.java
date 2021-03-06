package net.emaze.networks.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import net.emaze.networks.ipv6.Ipv6Network;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;

@ContextConfiguration(classes = InMemoryHibernateConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class Ipv6NetworkTypeTest {

    @Autowired
    private HibernateOperations hibernateOperations;
    @Autowired
    private TransactionTemplate tx;

    @Test
    public void canSerializeAndDeserializeAIpv6Network() {
        final Ipv6NetworkContainer container = new Ipv6NetworkContainer();
        container.setNetwork(Ipv6Network.fromCidrNotation("::/0"));
        final Serializable id = tx.execute((state) -> {
            return hibernateOperations.save(container);
        });

        hibernateOperations.execute(session -> {
            Ipv6NetworkContainer got = (Ipv6NetworkContainer) session.get(Ipv6NetworkContainer.class, id);
            Assert.assertEquals(Ipv6Network.fromCidrNotation("::/0"), got.getNetwork());
            return got;
        });
    }

    @Test
    public void canUseNetworkAsPrimaryKey() {
        final Ipv6NetworkKeyContainer container = new Ipv6NetworkKeyContainer();
        container.setNetwork(Ipv6Network.fromCidrNotation("::/0"));
        final Serializable id = tx.execute((state) -> {
            return hibernateOperations.save(container);
        });

        hibernateOperations.execute(session -> {
            Ipv6NetworkKeyContainer got = (Ipv6NetworkKeyContainer) session.get(Ipv6NetworkKeyContainer.class, id);
            Assert.assertEquals(Ipv6Network.fromCidrNotation("::/0"), got.getNetwork());
            return got;
        });
    }

    @Entity
    @Table(name = "networkv6_container")
    public static class Ipv6NetworkContainer {

        @Id
        @GeneratedValue
        private Integer id;
        private Ipv6Network network;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Ipv6Network getNetwork() {
            return network;
        }

        public void setNetwork(Ipv6Network network) {
            this.network = network;
        }
    }

    @Entity
    @Table(name = "networkv6_key_container")
    public static class Ipv6NetworkKeyContainer {

        @Id
        private Ipv6Network network;

        public Ipv6Network getNetwork() {
            return network;
        }

        public void setNetwork(Ipv6Network network) {
            this.network = network;
        }
    }
}
