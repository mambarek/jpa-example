package com.it2go.jpa.services;

import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.Employee;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

//@Ignore
@RunWith(Arquillian.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class EmployeeRepositoryServiceTest {

    EmployeeRepositoryService service;

    @Deployment(testable = true)
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
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");


        // choose your packaging here
        return war;
    }

    @ArquillianResource
    private URL deploymentURL;

    private EmployeeRepositoryService getEmployeeService(){
        String uri = deploymentURL.toString();
        String serviceWSDL =  uri + "EmployeeRepositoryService?wsdl";
        final QName serviceName
                = new QName("http://services.jpa.it2go.com/", "EmployeeRepositoryService");
        final QName portName
                = new QName("http://services.jpa.it2go.com/", "EmployeeRepositoryServiceImplPort");

        Service service = null;
        try {
            service = Service.create(new URL(serviceWSDL), serviceName);
            return service.getPort(EmployeeRepositoryService.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //EmployeeRepositoryService employeeRepositoryService = service.getPort(portName, EmployeeRepositoryService.class);
       return null;
    }
    @Test
    public void test(){
        System.out.println("Hello from EmployeeRepositoryServiceTest!!!!!!!!");
        System.out.println("++++ deploymentURL = " + deploymentURL);

        try {
            String uri = deploymentURL.toString() + "/EmployeeRepositoryService?wsdl";
            URL url = new URL(uri);
            final URLConnection connection = url.openConnection();
            connection.getInputStream();

            InputStream iStream =  connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream, "utf8"));
            StringBuffer sb = new StringBuffer();
            String line = "";

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            System.out.println(sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    //@RunAsClient
    public void test00() throws MalformedURLException {

        //EmployeeRepositoryService employeeRepositoryService = service.getPort(portName, EmployeeRepositoryService.class);
        EmployeeRepositoryService employeeRepositoryService = this.getEmployeeService();
        Employee employee = new Employee();
        employee.setFirstName("xxx");
        employee.setLastName("yyy");
        employee.setSalary(5000);

        Employee res = null;
        try {
            res = employeeRepositoryService.save(employee);
        } catch (EntityConcurrentModificationException e) {
            e.printStackTrace();
        } catch (EntityRemovedException e) {
            e.printStackTrace();
        }

        System.out.println(">>>> res = " + res);
    }

    @Test
    public void test01(){
        EmployeeRepositoryService employeeRepositoryService = this.getEmployeeService();
        final Employee employeeById = employeeRepositoryService.getEmployeeById(1L);
        System.out.println("employeeById = " + employeeById);
    }

    @Test
    public void test02(){
        EmployeeRepositoryService employeeRepositoryService = this.getEmployeeService();
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Braun");
        employee.setSalary(3500);

        try {
            employeeRepositoryService.save(employee);
        } catch (EntityConcurrentModificationException e) {
            e.printStackTrace();
        } catch (EntityRemovedException e) {
            e.printStackTrace();
        }

        final List<Employee> employees = employeeRepositoryService.findAll();
        System.out.println("-- Print all Employees");
        employees.forEach(System.out::println);
    }
}