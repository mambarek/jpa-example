package com.it2go.jpa.logic;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.Car;
import com.it2go.jpa.entities.EmailAddress;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Gender;
import com.it2go.jpa.entities.Person;
import com.it2go.jpa.entities.Project;
import com.it2go.jpa.entities.Truck;
import com.it2go.jpa.persistence.ICarRepository;
import com.it2go.jpa.persistence.IEmployeeRepository;
import com.it2go.jpa.persistence.IPersonRepository;
import com.it2go.jpa.persistence.ITruckRepository;
import lombok.Builder;
import lombok.Singular;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class EmployeeClient {

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

    private void testRepo1() throws EntityConcurrentModificationException, EntityRemovedException {

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

        admin = personRepository.persist(admin, userSession.getTestCreationUser());
        update = personRepository.persist(update, userSession.getTestUpdateUser());

        //admin.setBirthDate(Date.from(LocalDate.of(1958,6,1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        //personRepository.persist(admin);

        // Concurrent modification rise exception
        //admin.setBirthDate(Date.from(LocalDate.of(1988,9,10).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        //personRepository.persist(admin);

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

        anna = employeeRepository.persist(anna, userSession.getTestCreationUser());
        john = employeeRepository.persist(john, userSession.getTestCreationUser());
        helena = employeeRepository.persist(helena, userSession.getTestCreationUser());

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
        employeeRepository.persist(emp2, userSession.getTestCreationUser());

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

    public void testCast() {
        Employee e = new Employee();
        Person p = (Person) e;

        final boolean b = e instanceof Person;
        System.out.println("b = " + b);
        final boolean equals = e.getClass().equals(Person.class);
        System.out.println("equals = " + equals);
        final boolean equals1 = e.getClass().equals(Employee.class);
        System.out.println("equals1 = " + equals1);


    }

    public void testValidation() throws EntityConcurrentModificationException, EntityRemovedException {
        Person p = new Person();
        p.setLastName("man");
        // p.setFirstName("12");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        final Set<ConstraintViolation<Person>> constraintViolations = validator.validate(p);
        for (ConstraintViolation violation : constraintViolations) {
            System.out.println("violation = " + violation.getMessage());
        }

        personRepository.persist(p, userSession.getTestCreationUser());
    }

    public void testEmbeddedEntities() throws EntityConcurrentModificationException, EntityRemovedException {

        Person carOwner = new Person();
        carOwner.setFirstName("Bill");
        carOwner.setLastName("Gates");
        carOwner = personRepository.persist(carOwner, userSession.getTestCreationUser());

        //carOwner = personRepository.findById(carOwner.getId());
        carOwner.setLastName("xxx-y");
        carOwner = personRepository.persist(carOwner, userSession.getTestCreationUser());

        Person driver1 = new Person();
        driver1.setFirstName("driver1");
        driver1.setLastName("driver");

        Person driver2 = new Person();
        driver2.setFirstName("driver2");
        driver2.setLastName("driver");

        driver1 = personRepository.persist(driver1, userSession.getTestCreationUser());
        driver2 = personRepository.persist(driver2, userSession.getTestCreationUser());

        Car car = new Car();
        car.setDoorsCount(5);
        car.setEnginePower(180);
        car.setWheelsCount(4);
        car.getOptions().add("Car-option-1");
        car.getOptions().add("Car-option-2");
        car.getOptions().add("Car-option-3");

        car.setOwner(carOwner);
        car.getDrivers().add(driver1);
        //car.getDrivers().add(driver2);

        carRepository.persist(car, userSession.getTestCreationUser());

        Truck truck0 = new Truck();
        truckRepository.persist(truck0, userSession.getTestCreationUser());

        Truck truck = new Truck();
        truck.setDoorsCount(2);
        truck.setEnginePower(500);
        truck.setTrailerLength(8);
        truck.setWheelsCount(6);
        truck.getOptions().add("Truck-option-1");
        truck.getOptions().add("Truck-option-2");

        truck.getDrivers().add(driver2);
        truckRepository.persist(truck, userSession.getTestCreationUser());


        final List<Car> carList = carRepository.findAll();
        final List<Truck> truckList = truckRepository.findAll();

        System.out.println("truckList = " + truckList);
    }

    public static void main(String[] args) {

/*        Weld weld = new Weld();
       // try {
            WeldContainer weldContainer = weld.initialize();
            BeanManager beanManager = weldContainer.getBeanManager();

            final WeldInstance<EmployeeClient> employeeClientInstance = weldContainer.select(EmployeeClient.class);
            final EmployeeClient employeeClient = employeeClientInstance.get();

            employeeClient.testRepo1();
            //employeeClient.testValidation();
            // employeeClient.testCast();
            employeeClient.testEmbeddedEntities();*/

/*
        } finally {
            weld.shutdown();
        }
*/

//Person p = personBuilder().age(3).job("erster").job("zweiter").vorname("Ali").nachname("Mbarek").build();
    }


    @Builder(builderClassName = "PersonBuilder", builderMethodName = "personBuilder")
    public static Person createPerson(String vorname, String nachname, int age, @Singular List<String> jobs) {
        return null;
    }
}
