package com.blazeclan.ElasticsearchWithSpringBoot.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.common.collect.Map;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blazeclan.ElasticsearchWithSpringBoot.entities.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EmployeeRepository implements EmployeeRepo {
	
	/*
	 * Name of the index and type
	 */
	String INDEX = "employee";  
    String TYPE = "_doc";
	
	@Autowired
	private ObjectMapper objectMapper;

	/*
	 * Rest-high-level client for communicating with elastic-search
	 */
	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http")));
	
	@Override
	public List<Employee> getAllEmp() {
		// TODO Auto-generated method stub
		SearchRequest searchRequest = new SearchRequest(); // response for the search in elastic-search
		searchRequest.indices("employee"); // Name of index is employee
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // Used to query the elastic-search
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); //Fetching all data
		searchRequest.source(searchSourceBuilder); // storing data into searchRequest
		List<Employee> employeelist = new ArrayList<>(); //List for string map result
		SearchResponse searchResponse= null;  // used to sending the response 
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		 // send via client for searchRequest
			if(searchResponse.getHits().getTotalHits().value>0) {
				org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits(); // getting all the hits
				for(org.elasticsearch.search.SearchHit hit:searchHit) {
					Map <String, Object> map = hit.getSourceAsMap();  //know data is in map format
					employeelist.add(objectMapper.convertValue(map, Employee.class)); //converting map to Employee class and return as a list
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employeelist;
	}

	@Override
	public List<Employee> getEmpByNameFromElasticSearch(String userName) {
		// TODO Auto-generated method stub
		SearchRequest searchRequest = new SearchRequest(); // response for the search in elastic-search
		searchRequest.indices("employee"); // Name of index is employee
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // Used to query the elastic-search
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName.keyword", userName))); //Fetching all data using keyword analyzer not token analyzer
		searchRequest.source(searchSourceBuilder); // storing data into searchRequest
		List<Employee> employeelist = new ArrayList<>(); //List for string map result
		SearchResponse searchResponse= null;  // used to sending the response 
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		 // send via client for searchRequest
			if(searchResponse.getHits().getTotalHits().value>0) {
				org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits(); // getting all the hits
				for(org.elasticsearch.search.SearchHit hit:searchHit) {
					Map <String, Object> map = hit.getSourceAsMap();  //know data is in map format
					employeelist.add(objectMapper.convertValue(map, Employee.class)); //converting map to Employee class and return as a list
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employeelist;
	}

	@Override
	public List<Employee> getEmpInfoByNameAndAddressFromElasticSearch(String userName, String address) {
		// TODO Auto-generated method stub
		SearchRequest searchRequest = new SearchRequest(); // response for the search in elastic-search
		searchRequest.indices("employee"); // Name of index is employee
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // Used to query the elastic-search
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("userName.keyword", userName)) //Fetching all data using keyword analyzer not token analyzer mostly used in duplicate records
				                                            .must(QueryBuilders.matchQuery("address", address))); // matchQuery is for normal matching i.e address
		searchRequest.source(searchSourceBuilder); // storing data into searchRequest
		List<Employee> employeelist = new ArrayList<>(); //List for string map result
		SearchResponse searchResponse= null;  // used to sending the response 
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		 // send via client for searchRequest
			if(searchResponse.getHits().getTotalHits().value>0) {
				org.elasticsearch.search.SearchHit[] searchHit = searchResponse.getHits().getHits(); // getting all the hits
				for(org.elasticsearch.search.SearchHit hit:searchHit) {
					Map <String, Object> map = hit.getSourceAsMap();  //know data is in map format
					employeelist.add(objectMapper.convertValue(map, Employee.class)); //converting map to Employee class and return as a list
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return employeelist;
	}

	@Override
	public String deleteEmpFromElasticSearch(String id) throws Exception {
		// TODO Auto-generated method stub
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		DeleteResponse response = client.delete(deleteRequest,RequestOptions.DEFAULT);
		return response
                .getResult()
                .name();
	}

	@Override
	public String createEmployeeDocument(Employee employee) throws IOException {
		// TODO Auto-generated method stub
		
//		UUID uuid = UUID.randomUUID();
//		employee.setUserId(uuid.toString());
		/*
		 * know we have data into object form we have to converted into map format
		 */
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, employee.getUserId()).source(EmployeeObjectToMap(employee), XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT); 
		return  indexResponse.getResult().name();
	}
	
	private Map<String, Object> EmployeeObjectToMap(Employee employee){
		return objectMapper.convertValue(employee, Map.class);
	}

	@Override
	public String updateEmpFromElasticSearch(Employee employee) throws Exception {
		// TODO Auto-generated method stub
		UpdateRequest updateRequest = new UpdateRequest(
                INDEX,
                TYPE,
                employee.getUserId());
		updateRequest.doc(EmployeeObjectToMap(employee));
		UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
		
		 return updateResponse
                 .getResult()
                 .name();
	}
	

}
