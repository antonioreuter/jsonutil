package com.waes.comparators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.waes.models.JsonDiff;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by aandra1 on 30/09/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonComparatorTest {

  @Spy
  private JsonComparator subject;

  @Test
  public void whenPayloadsAreTheSame() {
    String payloadLeft = retrievePayload("payloads/order_10.json");
    String payloadRight = retrievePayload("payloads/order_10.json");

    JsonDiff<String, Object> jsonDiff = subject.compare(payloadLeft, payloadRight);
    Assert.assertTrue(jsonDiff.isIdentical());
    Assert.assertTrue(jsonDiff.isSameSize());
    Assert.assertEquals(0, jsonDiff.getEntriesOnlyOnLeft().size());
    Assert.assertEquals(0, jsonDiff.getEntriesOnlyOnRight().size());
    Assert.assertEquals(0, jsonDiff.getEntriesDiffering().size());
    Assert.assertEquals(convertJsonToMap(payloadLeft), jsonDiff.getEntriesInCommon());
  }

  @Test
  public void whenPayloadsHaveDifferentValues() {
    String payloadLeft = retrievePayload("payloads/order_10.json");
    String payloadRight = retrievePayload("payloads/order_10_1.json");

    String payloadInCommon = "{\"order\":10.0}";
    String payloadDiff = "{\"client\":{\"left\":{\"document\":\"123456\",\"name\":\"Antonio Reuter\"},\"right\":{\"document\":\"987945\",\"name\":\"Antonio Reuter\"}},\"payment\":{\"left\":{\"type\":\"Credit Card\",\"value\":15.3},\"right\":{\"type\":\"Credit Card\",\"value\":22.5}}}";

    JsonDiff<String, Object> jsonDiff = subject.compare(payloadLeft, payloadRight);
    Assert.assertFalse(jsonDiff.isIdentical());
    Assert.assertTrue(jsonDiff.isSameSize());
    Assert.assertEquals(0, jsonDiff.getEntriesOnlyOnLeft().size());
    Assert.assertEquals(0, jsonDiff.getEntriesOnlyOnRight().size());
    Assert.assertEquals(2, jsonDiff.getEntriesDiffering().size());
    Assert.assertEquals(convertJsonToMap(payloadInCommon), jsonDiff.getEntriesInCommon());
    Assert.assertEquals(convertJsonToMap(payloadDiff), jsonDiff.getEntriesDiffering());
  }

  @Test
  public void whenPayloadsHaveDifferentKeys() {
    String payloadLeft = retrievePayload("payloads/order_10.json");
    String payloadRight = retrievePayload("payloads/order_20.json");

    String payloadInCommon = "{\"order\":10.0,\"payment\":{\"type\":\"Credit Card\",\"value\":15.3}}";
    String payloadDiff = "{}";
    String payloadOnlyOnLeft = "{\"client\":{\"document\":\"123456\",\"name\":\"Antonio Reuter\"}}";
    String payloadOnlyOnRight = "{\"seller\":{\"id\":52.0,\"name\":\"Walmart.com\"}}";

    JsonDiff<String, Object> jsonDiff = subject.compare(payloadLeft, payloadRight);
    Assert.assertFalse(jsonDiff.isIdentical());
    Assert.assertFalse(jsonDiff.isSameSize());
    Assert.assertEquals(1, jsonDiff.getEntriesOnlyOnLeft().size());
    Assert.assertEquals(1, jsonDiff.getEntriesOnlyOnRight().size());
    Assert.assertEquals(0, jsonDiff.getEntriesDiffering().size());
    Assert.assertEquals(convertJsonToMap(payloadInCommon), jsonDiff.getEntriesInCommon());
    Assert.assertEquals(convertJsonToMap(payloadDiff), jsonDiff.getEntriesDiffering());
    Assert.assertEquals(convertJsonToMap(payloadOnlyOnLeft), jsonDiff.getEntriesOnlyOnLeft());
    Assert.assertEquals(convertJsonToMap(payloadOnlyOnRight), jsonDiff.getEntriesOnlyOnRight());
  }

  private <K, V> Map<String, Object> convertJsonToMap(String payload) {
    Gson gson = new GsonBuilder().create();
    Type type = new TypeToken<Map<K, V>>(){}.getType();

    return gson.fromJson(payload, type);
  }

  private String retrievePayload(String fileName){
    String payload = "";

    ClassLoader classLoader = getClass().getClassLoader();
    try {
      payload = IOUtils.toString(classLoader.getResourceAsStream(fileName), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return payload;
  }
}
