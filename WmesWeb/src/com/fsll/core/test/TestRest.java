package com.fsll.core.test;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class TestRest {
	@Autowired
	RestTemplate restTemplate;
	
	public static  final String URL = "http://localhost/wmes/service/exchangeRate/getCurrencyList.r";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> test2() {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			HashMap<String, String> uriVariables = new HashMap<String, String>();
			uriVariables.put("type","xxx");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

			ResponseEntity<Map> body = restTemplate.exchange(URL, HttpMethod.POST, requestEntity, Map.class,uriVariables);
					resultMap =	body.getBody();
			return resultMap;
		} catch (Exception e) {
//			logger.error("sync chat error：" + e);
		}
		return null;
	}
	
	//测试成功
	public void test1() {
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        
//        JSONObject jsonObj = JSONObject.fromObject(params);
        JSONObject jsonObj = new JSONObject();
        HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);

        String result = restTemplate.postForObject(URL, formEntity, String.class);
        result = result + "";
        //System.out.println(result);
	}
	
    public static void main(String[] args) {
    	//System.out.println("Rest ws test start .............");
//    	RestTemplate client = SimpleRestClient.getClient();
    	
//    	RestTemplate restTemplate = new RestTemplate();
//
//    	MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
//    	mockServer.expect(requestTo("/greeting")).andRespond(withSuccess("Hello world", MediaType.TEXT_PLAIN));
//
//    	// use RestTemplate ...
//
//    	mockServer.verify();
    	
    	TestRest testRest = new TestRest();
    	testRest.test1();

//    	Map<String, Object> map = testRest.test2();
//    	System.out.println(map.toString());
    	
        //System.out.println("Rest ws test done.");
    }
}

