package com.it2go.jpa.dao.cmt;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.EmailAddress;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Gender;
import com.it2go.jpa.entities.Person;
import com.it2go.jpa.entities.Project;
import com.it2go.jpa.persistence.ICarRepository;
import com.it2go.jpa.persistence.IEmployeeRepository;
import com.it2go.jpa.persistence.IPersonRepository;
import com.it2go.jpa.persistence.ITruckRepository;
import com.it2go.jpa.view.EmployeeViewRepository;
import com.it2go.jpa.view.model.EmployeeTableItem;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.EJB;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Ignore
public class EntityCmtDAOArquillianTest {

    @EJB
    private UserSession userSession;
    @EJB
    private IEmployeeRepository employeeRepository;
    @EJB
    private IPersonRepository personRepository;
    @EJB
    private ICarRepository carRepository;
    @EJB
    private ITruckRepository truckRepository;
    @EJB
    private EmployeeViewRepository employeeViewRepository;

    // test for concurrency uodate
    private static Employee cachedEmplJohn;
    private static Employee cachedEmplHelena;
    private static Long helenaId;

    @Deployment
    public static Archive<?> createDeployment() {

        // get all maven dependecies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        // You can use war packaging...
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                //.addPackage(Employee.class.getPackage())
                .addPackages(true, "com.it2go.jpa")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("wildfly-ds.xml")
                //.addAsLibraries(files)
                //.addAsLibraries(resolver.artifact("com.it2go:jpa-model:1.0-SNAPSHOT").resolveAsFiles())
                //.addAsWebInfResource("jbossas-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");


        // choose your packaging here
        return war;
    }

    @Test //PrepaireData
    public void test00() {
        System.out.println("### test0");
        // create admin user
        Person admin = new Person();
        admin.setLastName("man");
        admin.setFirstName("Admin");
        admin.setEmbeddedEmail(new EmailAddress("ali@mbarek"));

        LocalDate date = LocalDate.of(1970, 1, 1);
        admin.setBirthDate(Date.valueOf(date));

/*        admin.getEmails().add("admin@mail.com");
        admin.getEmails().add("admin@hotmail.com");
        admin.getEmails().add("admin@google.com");*/

        admin.getEmails().add(new EmailAddress("admin@mail.com"));
        admin.getEmails().add(new EmailAddress("admin@hotmail.com"));
        admin.getEmails().add(new EmailAddress("admingooglecom"));
        admin.getEmails().add(new EmailAddress());
        //admin.setMymal("xxx");

        Person update = new Person();
        update.setLastName("man");
        update.setFirstName("Update");

        try {
            admin = personRepository.persist(admin, null);
            update = personRepository.persist(update, null);
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(admin.isNew());
        Assert.assertFalse(update.isNew());
    }

    //@Test // Test EJB
    public void test02(){
        System.out.println("### test_02_EJB()");
        Employee employee = new Employee();
        employee.setFirstName("XXX");
        employee.setLastName("YYY");

        try {
            employee = employeeRepository.persist(employee, null);
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        System.out.println(">>>> employee = " + employee);
    }

    @Test // test Lazy loading
    public void test01(){
        System.out.println("### test1");
        Employee anna = new Employee();
        anna.setFirstName("Anna");
        anna.setLastName("Barth");
        anna.setSalary(3000);
        anna.setGender(Gender.female);

        anna.getEmails().add(new EmailAddress("ann@mobil.com"));
        anna.getEmails().add(new EmailAddress("ann2@mobil.com"));

        Person paul = new Person();
        paul.setFirstName("Paul");
        paul.setLastName("Barth");

        Person eva = new Person();
        eva.setFirstName("Eva");
        eva.setLastName("Barth");

        anna.addChild(paul);
        anna.addChild(eva);

        try {
            anna = employeeRepository.persist(anna, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        // final Class<? extends List> aClass = anna.getChildren().getClass();

       // System.out.println("aClass = " + aClass);
       // final int childrenSize = anna.getChildren().size();

      //  System.out.println("childrenSize = " + childrenSize);

        //anna.getChildren().forEach(System.out::println);
        Employee anna2 = employeeRepository.findById(anna.getId());
        Assert.assertEquals(2, anna2.getChildren().size());

        anna2.getChildren().forEach(System.out::println);
    }

    @Test
    public void test03(){
        System.out.println("### test3");
        final List<Person> children = personRepository.findByFullName("Paul", "Barth");
        for (Person p : children) {
            System.out.println("Parents of child = " + p);
            Assert.assertEquals(1,p.getParents().size());
            p.getParents().forEach(System.out::println);
        }
    }

    @Test
    public void test04(){
        Employee john = new Employee();
        john.setFirstName("John");
        john.setLastName("Smith");
        john.setSalary(4500);

        Employee helena = new Employee();
        helena.setFirstName("Helena");
        helena.setLastName("Mayer");
        helena.setGender(Gender.female);
        helena.setSalary(3800);

        Project p1 = new Project();
        p1.setName("My Project 1");

        Project p2 = new Project();
        p2.setName("My Project 2");

        Project p3 = new Project();
        p3.setName("My Project 3");

        //john.setProjects(Arrays.asList(p1, p2));
        john.addProject(p1);
        john.addProject(p2);

        helena.addProject(p3);

        //john = employeeRepository.persist(john, userSession.getTestCreationUser());
        try {
            john = employeeRepository.persist(john, null);
            helena = employeeRepository.persist(helena, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        cachedEmplJohn = john;
        cachedEmplHelena = helena;
        helenaId = helena.getId();

        // Test getById
        System.out.println("--> test getById() : " + john.getId());
        Employee byId = employeeRepository.findById(john.getId());
        System.out.println("byId = " + byId);
        byId.setSalary(10000);
        try {
            byId = employeeRepository.persist(byId, userSession.getTestUpdateUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test05(){
        System.out.println("### test5");
        final List<EmployeeTableItem> employeeItems = employeeViewRepository.getEmployeeItems();
        employeeItems.forEach(System.out::println);

        Map<String, Object> map = new HashMap<>();
        map.put("firstName","Anna");
        final List<EmployeeTableItem> employeeItems1 = employeeViewRepository.getEmployeeItems(map);
        employeeItems1.forEach(System.out::println);
    }

    @Test
    public void test06(){
        System.out.println("### test6");
        LocalDate date = LocalDate.of(1970, 1, 1);
        System.out.println("-- Print all male employees NOCHMAL!!!!!");

        final List<Employee> all_Male = employeeRepository.findByGender(Gender.male);
        all_Male.forEach(System.out::println);

        System.out.println(" ## Test getByBirthday");
        final List<Person> byBirthDate = personRepository.findByBirthDate(date);
        System.out.println("byBirthDate = " + byBirthDate);
    }

    @Test
    public void test07(){
        System.out.println("### test7");
        final List<EmployeeTableItem> employeeTableItems = employeeViewRepository.testCriteria();
        employeeTableItems.forEach(System.out::println);
    }

    @Test
    public void test08(){
        System.out.println("### test8");
        final List<Employee> employees = employeeRepository.findByFirstName("John");
        for(Employee employee: employees){
            System.out.println("employeeRepository getLastUpdate empl="+ employee + " lastUpdate= " + employeeRepository.getLastUpdate(employee));
        }

    }

    @Test(expected = EntityConcurrentModificationException.class)
    public void test09() throws EntityConcurrentModificationException {
        System.out.println("### test9");
        cachedEmplJohn.setSalary(2000);
        try {
            this.employeeRepository.persist(cachedEmplJohn, userSession.getTestUpdateUser());
        } catch (EntityRemovedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test10(){
        System.out.println("### test10");
        System.out.println("cachedEmplHelena = " + cachedEmplHelena);
        try {
            employeeRepository.remove(cachedEmplHelena);
        } catch (EntityNotPersistedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11(){
        System.out.println("### test11");
        final Employee helena = employeeRepository.findById(helenaId);
        Assert.assertNull(helena);
    }

    //@Test
    public void test_02_Repo() {

        // create admin user
        Person admin = new Person();
        admin.setLastName("man");
        admin.setFirstName("Admin");
        admin.setEmbeddedEmail(new EmailAddress("ali@mbarek"));

        LocalDate date = LocalDate.of(1970, 1, 1);
        admin.setBirthDate(Date.valueOf(date));

/*        admin.getEmails().add("admin@mail.com");
        admin.getEmails().add("admin@hotmail.com");
        admin.getEmails().add("admin@google.com");*/

        admin.getEmails().add(new EmailAddress("admin@mail.com"));
        admin.getEmails().add(new EmailAddress("admin@hotmail.com"));
        admin.getEmails().add(new EmailAddress("admingooglecom"));
        admin.getEmails().add(new EmailAddress());
        //admin.setMymal("xxx");

        Person update = new Person();
        update.setLastName("man");
        update.setFirstName("Update");

        try {
            admin = personRepository.persist(admin, null);
            update = personRepository.persist(update, null);
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        Employee anna = new Employee();
        anna.setFirstName("Anna");
        anna.setLastName("Barth");
        anna.setSalary(3000);
        anna.setGender(Gender.female);

        anna.getEmails().add(new EmailAddress("ann@mobil.com"));
        anna.getEmails().add(new EmailAddress("ann2@mobil.com"));

        Person paul = new Person();
        paul.setFirstName("Paul");
        paul.setLastName("Barth");

        Person eva = new Person();
        eva.setFirstName("Eva");
        eva.setLastName("Barth");

        anna.addChild(paul);
        anna.addChild(eva);

        Employee john = new Employee();
        john.setFirstName("John");
        john.setLastName("Smith");
        john.setSalary(4500);

        Employee helena = new Employee();
        helena.setFirstName("Helena");
        helena.setLastName("Mayer");
        helena.setGender(Gender.female);
        helena.setSalary(3800);

        Project p1 = new Project();
        p1.setName("My Project 1");

        Project p2 = new Project();
        p2.setName("My Project 2");

        Project p3 = new Project();
        p3.setName("My Project 3");

        //john.setProjects(Arrays.asList(p1, p2));
        john.addProject(p1);
        john.addProject(p2);

        helena.addProject(p3);

        try {
            anna = employeeRepository.persist(anna, userSession.getTestCreationUser());
            john = employeeRepository.persist(john, userSession.getTestCreationUser());
            helena = employeeRepository.persist(helena, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        // Test getById
        System.out.println("--> test getById() : " + john.getId());
        final Employee byId = employeeRepository.findById(john.getId());
        System.out.println("byId = " + byId);

        final List<Employee> foundList = employeeRepository.findByLastName("Smith");
        Employee emp = foundList.get(0);
        System.out.println("emp = " + emp);

        emp.setSalary(7500);
//        employeeRepository.persist(emp);

        final List<Employee> foundList2 = employeeRepository.findByLastName("Smith");
        Employee emp2 = foundList2.get(0);
        System.out.println("emp = " + emp2);

/*
        final List<Employee> all = employeeRepository.findAll();
        all.forEach(System.out::println);

        final List<EmployeeTableItem> items = employeeRepository.getEmployeeItems(null);
        items.forEach(System.out::println);*/

        //emp2.removeProject(emp2.getProjects().get(0));
        emp2.setSalary(10);
        Project p4 = new Project();
        p4.setName("My Project 4");
        emp2.addProject(p4);
        try {
            employeeRepository.persist(emp2, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        // final List<EmployeeTableItem> items2 = employeeRepository.getEmployeeItems(null);
        // items2.forEach(System.out::println);

        final List<Employee> foundList3 = employeeRepository.findByLastName("Smith");
        Employee emp3 = foundList2.get(0);
        System.out.println("emp = " + emp3);

        System.out.println("-- Print all male employees");
        final List<Employee> allMale = employeeRepository.findByGender(Gender.male);
        allMale.forEach(System.out::println);

        final List<Person> children = personRepository.findByFullName("Paul", "Barth");
        for (Person p : children) {
            System.out.println("Parents of child = " + p);
            p.getParents().size();
            p.getParents().forEach(System.out::println);
        }

        //employeeRepository.remove(john);

        System.out.println("-- Print all male employees NOCHMAL!!!!!");
        final List<Employee> all_Male = employeeRepository.findByGender(Gender.male);
        all_Male.forEach(System.out::println);

        // employeeRepository.remove(anna);

        System.out.println(" ## Test getByBirthday");
        final List<Person> byBirthDate = personRepository.findByBirthDate(date);
        System.out.println("byBirthDate = " + byBirthDate);

        personRepository.executeUpdate("update Person set firstName = 'Ali' where firstName = 'Eva'");

        System.out.println(" ## Test testCriteriaAPI");
        final List<Person> people = personRepository.testCriteriaAPI();
       // people.forEach(System.out::println); not yet implemented

        System.out.println(" ## Test person getAll");
        final List<Person> allperson = personRepository.findAll();
        allperson.forEach(System.out::println);

        System.out.println(" ## Test person findByGender");
        final List<Person> byGender = personRepository.findByGender(Gender.male);
        byGender.forEach(System.out::println);
    }
    //@Test
    public void testRepo1() {

        // create admin user
        Person admin = new Person();
        admin.setLastName("man");
        admin.setFirstName("Admin");
        admin.setEmbeddedEmail(new EmailAddress("ali@mbarek"));

        LocalDate date = LocalDate.of(1970, 1, 1);
        admin.setBirthDate(Date.valueOf(date));

/*        admin.getEmails().add("admin@mail.com");
        admin.getEmails().add("admin@hotmail.com");
        admin.getEmails().add("admin@google.com");*/

        admin.getEmails().add(new EmailAddress("admin@mail.com"));
        admin.getEmails().add(new EmailAddress("admin@hotmail.com"));
        admin.getEmails().add(new EmailAddress("admingooglecom"));
        admin.getEmails().add(new EmailAddress());
        //admin.setMymal("xxx");

        Person update = new Person();
        update.setLastName("man");
        update.setFirstName("Update");

        try {
            admin = personRepository.persist(admin,null);
            update = personRepository.persist(update, null);
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        Employee anna = new Employee();
        anna.setFirstName("Anna");
        anna.setLastName("Barth");
        anna.setSalary(3000);
        anna.setGender(Gender.female);

        anna.getEmails().add(new EmailAddress("ann@mobil.com"));
        anna.getEmails().add(new EmailAddress("ann2@mobil.com"));

        Person paul = new Person();
        paul.setFirstName("Paul");
        paul.setLastName("Barth");

        Person eva = new Person();
        eva.setFirstName("Eva");
        eva.setLastName("Barth");

        anna.addChild(paul);
        anna.addChild(eva);

        Employee john = new Employee();
        john.setFirstName("John");
        john.setLastName("Smith");
        john.setSalary(4500);

        Employee helena = new Employee();
        helena.setFirstName("Helena");
        helena.setLastName("Mayer");
        helena.setGender(Gender.female);
        helena.setSalary(3800);

        Project p1 = new Project();
        p1.setName("My Project 1");

        Project p2 = new Project();
        p2.setName("My Project 2");

        Project p3 = new Project();
        p3.setName("My Project 3");

        //john.setProjects(Arrays.asList(p1, p2));
        john.addProject(p1);
        john.addProject(p2);

        helena.addProject(p3);

        try {
            anna = employeeRepository.persist(anna, userSession.getTestCreationUser());
            john = employeeRepository.persist(john, userSession.getTestCreationUser());
            helena = employeeRepository.persist(helena, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        // Test getById
        System.out.println("--> test getById() : " + john.getId());
        final Employee byId = employeeRepository.findById(john.getId());
        System.out.println("byId = " + byId);

        final List<Employee> foundList = employeeRepository.findByLastName("Smith");
        Employee emp = foundList.get(0);
        System.out.println("emp = " + emp);
        //this.employeeRepository.findByBirthDate(null);

        emp.setSalary(7500);
//        employeeRepository.persist(emp);

        final List<Employee> foundList2 = employeeRepository.findByLastName("Smith");
        Employee emp2 = foundList2.get(0);
        System.out.println("emp = " + emp2);

/*
        final List<Employee> all = employeeRepository.findAll();
        all.forEach(System.out::println);

        final List<EmployeeTableItem> items = employeeRepository.getEmployeeItems(null);
        items.forEach(System.out::println);*/

        //emp2.removeProject(emp2.getProjects().get(0));
        emp2.setSalary(10);
        Project p4 = new Project();
        p4.setName("My Project 4");
        emp2.addProject(p4);
        try {
            employeeRepository.persist(emp2, userSession.getTestCreationUser());
        } catch (EntityConcurrentModificationException | EntityRemovedException e) {
            e.printStackTrace();
        }

        // final List<EmployeeTableItem> items2 = employeeRepository.getEmployeeItems(null);
        // items2.forEach(System.out::println);

        final List<Employee> foundList3 = employeeRepository.findByLastName("Smith");
        Employee emp3 = foundList2.get(0);
        System.out.println("emp = " + emp3);

        System.out.println("-- Print all male employees");
        final List<Employee> allMale = employeeRepository.findByGender(Gender.male);
        allMale.forEach(System.out::println);

/*
        final List<Employee> byFullName = employeeRepository.findByFullName("Paul", "Barth");
        for (Employee empl: byFullName) {
            System.out.println("Parents of empl = " + empl);
            empl.getParents().forEach(System.out::println);
        }
*/

        final List<Person> children = personRepository.findByFullName("Paul", "Barth");
        for (Person p : children) {
            System.out.println("Parents of child = " + p);
            p.getParents().forEach(System.out::println);
        }

        //employeeRepository.remove(john);

        System.out.println("-- Print all male employees NOCHMAL!!!!!");
        final List<Employee> all_Male = employeeRepository.findByGender(Gender.male);
        all_Male.forEach(System.out::println);

        // employeeRepository.remove(anna);

        System.out.println(" ## Test getByBirthday");
        final List<Person> byBirthDate = personRepository.findByBirthDate(date);
        System.out.println("byBirthDate = " + byBirthDate);

        personRepository.executeUpdate("update Person set firstName = 'Ali' where firstName = 'Eva'");

        System.out.println(" ## Test testCriteriaAPI");
        final List<Person> people = personRepository.testCriteriaAPI();
        people.forEach(System.out::println);

        System.out.println(" ## Test person getAll");
        final List<Person> allperson = personRepository.findAll();
        allperson.forEach(System.out::println);

        System.out.println(" ## Test person findByGender");
        final List<Person> byGender = personRepository.findByGender(Gender.male);
        byGender.forEach(System.out::println);
    }

}
