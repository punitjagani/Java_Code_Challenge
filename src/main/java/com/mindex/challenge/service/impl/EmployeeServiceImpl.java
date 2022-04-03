package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
int count=0;
HashSet<String> empSet=new HashSet<>();
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    
    /**
     * Computes the total number of reports under an employee based on employee id
     */
    @Override
    public ReportingStructure getNoOfReports(String employeeId) {
		LOG.debug("Getting no of reports for employee with id [{}]", employeeId);
		
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
	
	    if (employee == null) {
	        throw new RuntimeException("Invalid employeeId: " + employeeId);
	    }
	    
	    //if there are no direct reports under an employee then return 0
	    if(employee.getDirectReports()==null) {
	    	ReportingStructure rs=new ReportingStructure(employee, 0);
	    	rs.setNumberOfReports(0);
	    	return rs;
	    }
	    
	   ReportingStructure rs=new ReportingStructure(employee, employee.getDirectReports().size());
       count=0;
       empSet=new HashSet<>();
       count= countDirectReports(employeeId, employee);
       rs.setNumberOfReports(count);
       return rs;
    }
    
    /**
     * Gets total number of direct reports under an employee
     * @param employeeId
     * @param employee
     * @return count total number of direct reports under an employee
     */
   
    private int countDirectReports(String employeeId, Employee employee) {

    	if(employee.getDirectReports()==null) {
    		return count;
    	}
    	else {
    		//count+=employee.getDirectReports().size();
    		for(Employee emp:employee.getDirectReports()) {
    			if(!empSet.contains(emp.getEmployeeId())) {
    				empSet.add(emp.getEmployeeId());
    				count+=1;
    			}
    		}
    		//Recursively call countDirectReports till we get count of direct reports null
    		for(Employee emp:employee.getDirectReports()) {
    			countDirectReports(emp.getEmployeeId(), employeeRepository.findByEmployeeId(emp.getEmployeeId()));
    		}
    	}
    	return count;
	}
    
    /**
     * Creates and saves the compensation object for the given employee id
     */
	@Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating Compensation [{}]", compensation);

        compensation.setEmployeeId(compensation.getEmployeeId());
        compensationRepository.insert(compensation);
        compensation.setEmployee(employeeRepository.findByEmployeeId(compensation.getEmployeeId()));
        return compensation;
    }
    
	/**
	 * Reads compensation details based on employee id
	 */
    @Override
    public Compensation readCompensation(String employeeId) {
    	  LOG.debug("Reading Compensation with employee id [{}]", employeeId);

    	  Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
    	  Employee employee = employeeRepository.findByEmployeeId(employeeId);
    	 
    	  if (compensation == null) {
              throw new RuntimeException("No compensation record found for employee id " + employeeId);
          }
          compensation.setEmployee(employee);

          return compensation;
    }
    
}
