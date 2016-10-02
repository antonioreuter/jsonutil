package com.waes.comparators;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.waes.models.JsonDiff;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by aandra1 on 30/09/16.
 *
 */
@Component("jsonComparator")
public class JsonComparator<K, V> {
  private Gson gson = new GsonBuilder().create();
  private Type type = new TypeToken<Map<K, V>>(){}.getType();

  /**
   * Compares two Jsons and return the differences.
   * founded between them.
   * @param payloadLeft - Payload on left side
   * @param payloadRight - Payload on right side
   * @return
   */
  public JsonDiff<K, V> compare(String payloadLeft, String payloadRight) {
    //Transforms the payload into a Map
    Map<K, V> mapPayloadLeft = gson.fromJson(payloadLeft, type);
    Map<K, V> mapPayloadRight = gson.fromJson(payloadRight, type);

    //Gets the size of the payload
    int payloadLeftSize = payloadLeft.getBytes().length;
    int payloadRightSize = payloadRight.getBytes().length;

    //Execute the diff, with the help of Maps
    MapDifference<K, V> mapDifference = Maps.difference(mapPayloadLeft,mapPayloadRight);

    //Creates a JsonDiff instance to gather the relevant information
    JsonDiff<K, V> jsonDiff = new JsonDiff<>(mapDifference, payloadLeftSize, payloadRightSize);

    return jsonDiff;
  }
}