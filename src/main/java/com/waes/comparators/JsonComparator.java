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
 */
@Component("jsonComparator")
public class JsonComparator<K, V> {
  private Gson gson = new GsonBuilder().create();
  private Type type = new TypeToken<Map<K, V>>(){}.getType();

  public JsonDiff<K, V> compare(String payloadLeft, String payloadRight) {
    Map<K, V> mapPayloadLeft = gson.fromJson(payloadLeft, type);
    Map<K, V> mapPayloadRight = gson.fromJson(payloadRight, type);

    int payloadLeftSize = payloadLeft.getBytes().length;
    int payloadRightSize = payloadRight.getBytes().length;

    MapDifference<K, V> mapDifference = Maps.difference(mapPayloadLeft,mapPayloadRight);
    JsonDiff<K, V> jsonDiff = new JsonDiff<>(mapDifference, payloadLeftSize, payloadRightSize);

    return jsonDiff;
  }
}