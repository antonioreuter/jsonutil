package com.waes.comparators;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by aandra1 on 30/09/16.
 */
@Component("jsonComparator")
public class JsonComparator {

  private Gson gson = new GsonBuilder().create();
  private Type type = new TypeToken<Map<String, Object>>(){}.getType();

  public MapDifference<String, Object> diff(String payloadLeft, String payloadRight) {
    Map<String, String> mapPayloadLeft = gson.fromJson(payloadLeft, type);
    Map<String, String> mapPayloadRight = gson.fromJson(payloadRight, type);

    return Maps.difference(mapPayloadLeft,mapPayloadRight);
  }
}
