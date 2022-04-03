package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
    
    /**
     * Gets total number of reportees under an employee 
     * @param id
     * @return ReportingStructure object which contains total number of reports under an employee
     */
    @GetMapping("/getNoOfReports/{id}")
    public ReportingStructure getNoOfReports(@PathVariable String id) {
        LOG.debug("Received request to get total number of reportees for id [{}]", id);

        return employeeService.getNoOfReports(id);
    }  
    
    /**
     * Creates and saves the compensation object for the given employee id
     * @param compensation
     * @return compensation object 
     */
    @PostMapping("/insertCompensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received create request for compensation", compensation);

        return employeeService.createCompensation(compensation);
    }
    
    /**
     * Gets compensation details based on employee id
     * @param id
     * @return compensation details
     */
    @GetMapping("/getCompensation/{id}")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received read create request for compensation by id [{}]", id);

        return employeeService.readCompensation(id);
    }
}
