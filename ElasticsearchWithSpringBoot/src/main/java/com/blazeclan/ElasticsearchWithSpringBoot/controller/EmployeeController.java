package com.blazeclan.ElasticsearchWithSpringBoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blazeclan.ElasticsearchWithSpringBoot.entities.Employee;
import com.blazeclan.ElasticsearchWithSpringBoot.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/test")
	public String testing() {
		return "Testing successfully for Elasticsearch up & running confirm man ...";
	}
	
	@PostMapping("/employee/create")
	public ResponseEntity createEmployee(@RequestBody Employee Employee) throws Exception {
		return new ResponseEntity(employeeService.createEmployeeDocument(Employee),HttpStatus.CREATED);
	}
	
	@GetMapping("/employee/allemp")
	public List<Employee> getAllEmp(){
		return employeeService.getAllEmp();
	}
	
	@GetMapping("/employee/{userName}")
	public List<Employee> getEmpByName(@PathVariable String userName){
		return employeeService.getEmpByName(userName);
	}
	
	@GetMapping("/employee/{userName}/{address}")
	public List<Employee> getEmpByNameAndAddress(@PathVariable String userName,@PathVariable String address){
		return employeeService.getEmpByNameAndAddress(userName,address);
	}
	
	@DeleteMapping("employee/delete/{id}")
	public ResponseEntity deleteEmp(@PathVariable String id) throws Exception {
		return new ResponseEntity(employeeService.deleteEmp(id), HttpStatus.ACCEPTED);
	}
	
	@PutMapping("employee/update")
	public ResponseEntity updateEmp(@RequestBody Employee employee)throws Exception {
		return new ResponseEntity(employeeService.updateEmp(employee), HttpStatus.OK);
	}
}
