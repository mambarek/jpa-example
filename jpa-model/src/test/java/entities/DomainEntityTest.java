package entities;

import com.it2go.jpa.entities.DomainEntity;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Person;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Ignore
public class DomainEntityTest {
    @Test
    public void testEquals() throws Exception {
    }

    @Test
    public void testHashCode() throws Exception {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        DomainEntity p1 = new Person();
        p1.setId(1L);
        p1.setLastUpdateTime(now);
        DomainEntity p2 = new Person();
        p2.setId(1L);
        p2.setLastUpdateTime(now);
        DomainEntity emp1 = new Employee();
        DomainEntity emp2 = new Employee();

        System.out.println("p1 = " + p1.hashCode());
        System.out.println("p2 = " + p2.hashCode());
        System.out.println("p1 equals p2 = " + p1.equals(p2));
        System.out.println("emp1 = " + emp1.hashCode());
        System.out.println("emp2 = " + emp2.hashCode());
    }

}