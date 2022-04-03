package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    
    /**
     * Tests the expected output with the output computed by getNoOfReports() by passing employee id
     */
    @Test
    public void getNoOfReports() {
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure rs =  employeeService.getNoOfReports(testEmployee.getEmployeeId());
        assertEquals(4, rs.getNumberOfReports());
    }
    
    /**
     * Tests both create and read compensation methods.
     * The compensation object is created first and is compared with the output of readCompensation method
     */
    @Test
    public void createReadCompensation() {
        Date TEST_DATE = Date.valueOf("2022-03-03");
        Long TEST_SALARY = 12000L;

        Compensation compensation = new Compensation();
        compensation.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        compensation.setSalary(12000L);
        compensation.setEffectiveDate(TEST_DATE);
        employeeService.createCompensation(compensation);
        Compensation readCompensation = employeeService.readCompensation(compensation.getEmployeeId());
        assertEquals(TEST_SALARY, readCompensation.getSalary());
    }
}
