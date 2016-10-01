package com.waes.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.MapDifference;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by aandra1 on 30/09/16.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDiff<K,V> implements Serializable {

  private boolean identical;

  private Map<K, V> entriesOnlyOnLeft;

  private Map<K, V> entriesOnlyOnRight;

  private Map<K, V> entriesInCommon;

  private Map<K, V> entriesDiffering;

  public JsonDiff(MapDifference<K, V> mapDifference) {
    this.identical = mapDifference.areEqual();
    this.entriesOnlyOnLeft = mapDifference.entriesOnlyOnLeft();
    this.entriesOnlyOnRight = mapDifference.entriesOnlyOnRight();
    this.entriesInCommon = mapDifference.entriesInCommon();
    this.entriesDiffering = convertDiffToMap(mapDifference.entriesDiffering());
  }

  private Map<K,V> convertDiffToMap(Map<K, MapDifference.ValueDifference<V>> diff) {
    return (new GsonBuilder().create())
        .fromJson(new com.google.gson.Gson().toJson(diff), new TypeToken<Map<String, Object>>(){}.getType());
  }
}