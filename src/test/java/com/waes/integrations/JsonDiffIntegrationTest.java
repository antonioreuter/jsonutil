package com.waes.integrations;

import com.waes.models.JsonCompareEntry;
import com.waes.models.JsonDiff;
import com.waes.models.JsonPayload;
import com.waes.models.PayloadPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by aandra1 on 01/10/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JsonDiffIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void createJsonCompareEntry() {
    JsonCompareEntry data = JsonCompareEntry.builder().name("Integration Test: createJsonCompareEntry").build();
    ResponseEntity<JsonCompareEntry> response =  this.restTemplate.postForEntity("/api/v1/diff", data, JsonCompareEntry.class );
    JsonCompareEntry jsonCompareEntryCreated = response.getBody();

    assertEquals(201, response.getStatusCodeValue());
    assertNotNull(jsonCompareEntryCreated.getId());
    assertEquals("Integration Test: createJsonCompareEntry", jsonCompareEntryCreated.getName());
  }

  @Test
  public void whenTryToCompareAndThereIsNoEntry() {
    ResponseEntity<JsonDiff> jsonDiffResponse = this.restTemplate.getForEntity("/api/v1/diff/{id}", JsonDiff.class, 100);

    assertEquals(404, jsonDiffResponse.getStatusCodeValue());
  }

  @Test
  public void whenTryToCompareAndThereIsJustOnePayloadRegistred() {
    JsonCompareEntry data = JsonCompareEntry.builder().name("Integration Test: whenTryToCompareAndThereIsJustOnePayloadRegistred").build();
    ResponseEntity<JsonCompareEntry> jsonCompareEntryResponse =  this.restTemplate.postForEntity("/api/v1/diff", data, JsonCompareEntry.class );
    JsonCompareEntry jsonCompareEntryCreated = jsonCompareEntryResponse.getBody();

    String payload = "{ \"order\": 10, \"client\": { \"name\": \"Jhon Doe\" } }";
    final HttpEntity<String> entityRequest = new HttpEntity<>(payload, customHeaders());
    ResponseEntity<JsonPayload> jsonPayloadResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/right", entityRequest, JsonPayload.class, jsonCompareEntryCreated.getId());

    ResponseEntity<JsonDiff> jsonDiffResponse = this.restTemplate.getForEntity("/api/v1/diff/{id}", JsonDiff.class, jsonCompareEntryCreated.getId());

    assertEquals(201, jsonCompareEntryResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadResponse.getStatusCodeValue());
    assertEquals(412, jsonDiffResponse.getStatusCodeValue());
  }

  @Test
  public void createJsonPayload() {
    JsonCompareEntry data = JsonCompareEntry.builder().name("Integration Test: createJsonPayload").build();
    ResponseEntity<JsonCompareEntry> jsonCompareEntryResponse =  this.restTemplate.postForEntity("/api/v1/diff", data, JsonCompareEntry.class );
    JsonCompareEntry jsonCompareEntryCreated = jsonCompareEntryResponse.getBody();

    String payload = "{ \"order\": 10, \"client\": { \"name\": \"Jhon Doe\" } }";
    final HttpEntity<String> entityRequest = new HttpEntity<>(payload, customHeaders());

    ResponseEntity<JsonPayload> jsonPayloadResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/right", entityRequest, JsonPayload.class, jsonCompareEntryCreated.getId());
    JsonPayload jsonPayloadCreated = jsonPayloadResponse.getBody();

    assertEquals(201, jsonCompareEntryResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadResponse.getStatusCodeValue());
    assertNotNull(jsonPayloadCreated.getId());
    assertEquals(PayloadPosition.RIGHT, jsonPayloadCreated.getPosition());
  }

  @Test
  public void compareJsonWithDifferentKeys() {
    JsonCompareEntry data = JsonCompareEntry.builder().name("Integration Test: compareJsonWithDifferentKeys").build();
    ResponseEntity<JsonCompareEntry> jsonCompareEntryResponse =  this.restTemplate.postForEntity("/api/v1/diff", data, JsonCompareEntry.class );
    JsonCompareEntry jsonCompareEntryCreated = jsonCompareEntryResponse.getBody();

    String payloadLeft = "{\"order\": 10, \"client\": { \"name\": \"Jhon Foe\" } }";
    final HttpEntity<String> entityRequestPayloadLeft = new HttpEntity<>(payloadLeft, customHeaders());
    ResponseEntity<JsonPayload> jsonPayloadLeftResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/left", entityRequestPayloadLeft, JsonPayload.class, jsonCompareEntryCreated.getId());

    String payloadRight = "{\"order\": 10, \"client\": { \"name\": \"Jhon Doe\" }, \"seller\": { \"id\": 15, \"name\": \"walmart.com\"} }";
    final HttpEntity<String> entityRequestPayloadRight = new HttpEntity<>(payloadRight, customHeaders());
    ResponseEntity<JsonPayload> jsonPayloadRightResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/right", entityRequestPayloadRight, JsonPayload.class, jsonCompareEntryCreated.getId());

    ResponseEntity<JsonDiff> jsonDiffResponse = this.restTemplate.getForEntity("/api/v1/diff/{id}", JsonDiff.class, jsonCompareEntryCreated.getId());
    JsonDiff jsonDiff = jsonDiffResponse.getBody();

    assertEquals(201, jsonCompareEntryResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadLeftResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadRightResponse.getStatusCodeValue());
    assertEquals(false, jsonDiff.isIdentical());
    assertEquals(false, jsonDiff.isSameSize());
    assertEquals("{}", jsonDiff.getEntriesOnlyOnLeft().toString());
    assertEquals("{seller={id=15.0, name=walmart.com}}", jsonDiff.getEntriesOnlyOnRight().toString());
    assertEquals("{order=10.0}", jsonDiff.getEntriesInCommon().toString());
    assertEquals("{client={left={name=Jhon Foe}, right={name=Jhon Doe}}}", jsonDiff.getEntriesDiffering().toString());
    assertEquals(48, jsonDiff.getPayloadLeftSize().intValue());
    assertEquals(94, jsonDiff.getPayloadRightSize().intValue());
  }

  @Test
  public void compareJsonWithDifferentValues() {
    JsonCompareEntry data = JsonCompareEntry.builder().name("Integration Test: compareJsonWithDifferentValues").build();
    ResponseEntity<JsonCompareEntry> jsonCompareEntryResponse =  this.restTemplate.postForEntity("/api/v1/diff", data, JsonCompareEntry.class );
    JsonCompareEntry jsonCompareEntryCreated = jsonCompareEntryResponse.getBody();

    String payloadLeft = "{\"order\": 10, \"client\": { \"name\": \"Jhon Doe\" } }";
    final HttpEntity<String> entityRequestPayloadLeft = new HttpEntity<>(payloadLeft, customHeaders());
    ResponseEntity<JsonPayload> jsonPayloadLeftResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/left", entityRequestPayloadLeft, JsonPayload.class, jsonCompareEntryCreated.getId());

    String payloadRight = "{\"order\": 10, \"client\": { \"name\": \"Jhon Low\" } }";
    final HttpEntity<String> entityRequestPayloadRight = new HttpEntity<>(payloadRight, customHeaders());
    ResponseEntity<JsonPayload> jsonPayloadRightResponse =  this.restTemplate.postForEntity("/api/v1/diff/{id}/right", entityRequestPayloadRight, JsonPayload.class, jsonCompareEntryCreated.getId());

    ResponseEntity<JsonDiff> jsonDiffResponse = this.restTemplate.getForEntity("/api/v1/diff/{id}", JsonDiff.class, jsonCompareEntryCreated.getId());
    JsonDiff jsonDiff = jsonDiffResponse.getBody();

    assertEquals(201, jsonCompareEntryResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadLeftResponse.getStatusCodeValue());
    assertEquals(201, jsonPayloadRightResponse.getStatusCodeValue());
    assertEquals(false, jsonDiff.isIdentical());
    assertEquals(true, jsonDiff.isSameSize());
    assertEquals("{}", jsonDiff.getEntriesOnlyOnLeft().toString());
    assertEquals("{}", jsonDiff.getEntriesOnlyOnRight().toString());
    assertEquals("{order=10.0}", jsonDiff.getEntriesInCommon().toString());
    assertEquals("{client={left={name=Jhon Doe}, right={name=Jhon Low}}}", jsonDiff.getEntriesDiffering().toString());
    assertEquals(48, jsonDiff.getPayloadLeftSize().intValue());
    assertEquals(48, jsonDiff.getPayloadRightSize().intValue());
  }

  private HttpHeaders customHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return headers;
  }

  @TestConfiguration
  static class Config {
    @Value("${api.username}")
    private String username;

    @Value("${api.password}")
    private String password;

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
      return new RestTemplateBuilder()
          .basicAuthorization(username, password);
    }
  }
}