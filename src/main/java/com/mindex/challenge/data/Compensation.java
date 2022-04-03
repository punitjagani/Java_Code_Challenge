package com.mindex.challenge.data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Punit Jagani
 *
 */
public class Compensation {

    private String employeeId;
	private Long salary;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date effectiveDate;
	private Employee employee;
		
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Compensation() {
		// TODO Auto-generated constructor stub
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
}
