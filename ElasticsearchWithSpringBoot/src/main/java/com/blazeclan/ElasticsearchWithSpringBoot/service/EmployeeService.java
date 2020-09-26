package com.blazeclan.ElasticsearchWithSpringBoot.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.blazeclan.ElasticsearchWithSpringBoot.entities.Employee;
import com.blazeclan.ElasticsearchWithSpringBoot.repository.EmployeeRepo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	public List<Employee> getAllEmp() {
		// TODO Auto-generated method stub
		return employeeRepo.getAllEmp();
	}

	public List<Employee> getEmpByName(String userName) {
		// TODO Auto-generated method stub
		return employeeRepo.getEmpByNameFromElasticSearch(userName);
	}

	public List<Employee> getEmpByNameAndAddress(String userName, String address) {
		// TODO Auto-generated method stub
		return employeeRepo.getEmpInfoByNameAndAddressFromElasticSearch(userName,address);
	}

	public Object deleteEmp(String id) throws Exception {
		// TODO Auto-generated method stub
		return employeeRepo.deleteEmpFromElasticSearch(id);
	}

	public String createEmployeeDocument(Employee employee) throws IOException {
		// TODO Auto-generated method stub
		return employeeRepo.createEmployeeDocument(employee);
	}

	public String updateEmp(Employee employee)throws Exception {
		// TODO Auto-generated method stub
		return employeeRepo.updateEmpFromElasticSearch(employee);
	}

	
}

