package com.blazeclan.ElasticsearchWithSpringBoot.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.blazeclan.ElasticsearchWithSpringBoot.entities.Employee;

@Repository
public interface EmployeeRepo {

	List<Employee> getAllEmp();

	List<Employee> getEmpByNameFromElasticSearch(String userName);

	List<Employee> getEmpInfoByNameAndAddressFromElasticSearch(String userName, String address);

	String deleteEmpFromElasticSearch(String id) throws Exception;

	String createEmployeeDocument(Employee employee) throws IOException;

	String updateEmpFromElasticSearch(Employee employee) throws Exception;

}
