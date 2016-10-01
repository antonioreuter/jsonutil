package com.waes.comparators;

import com.google.common.collect.MapDifference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import sun.misc.Unsafe;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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

    MapDifference<String, Object> diff = subject.diff(payloadLeft, payloadRight);
    Assert.assertTrue(diff.areEqual());
  }

  @Test
  public void whenPayloadsHaveDifferentValues() {
    String payloadLeft = retrievePayload("payloads/order_10.json");
    String payloadRight = retrievePayload("payloads/order_10_1.json");

    MapDifference<String, Object> diff = subject.diff(payloadLeft, payloadRight);
    Assert.assertFalse(diff.areEqual());
  }

  @Test
  public void whenPayloadsHaveDifferentKeys() {
    String payloadLeft = retrievePayload("payloads/order_10.json");
    String payloadRight = retrievePayload("payloads/order_20.json");

    MapDifference<String, Object> diff = subject.diff(payloadLeft, payloadRight);
    Assert.assertFalse(diff.areEqual());
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
