package com.waes.services;

import com.waes.comparators.JsonComparator;
import com.waes.exceptions.PayloadComparisonException;
import com.waes.exceptions.ResourceNotFoundException;
import com.waes.models.JsonCompareEntry;
import com.waes.models.JsonDiff;
import com.waes.models.JsonPayload;
import com.waes.models.PayloadPosition;
import com.waes.repositories.JsonCompareEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Created by aandra1 on 30/09/16.
 */
@Service("jsonCompareEntryService")
public class JsonCompareEntryService {

  @Autowired
  private JsonComparator jsonComparator;

  @Autowired
  private JsonCompareEntryRepository jsonCompareEntryRepository;

  /**
   * Finds a JsonComapareEntry by Id
   * @param id
   * @return
   */
  public JsonCompareEntry findById(long id) {
    JsonCompareEntry jsonCompareEntry = jsonCompareEntryRepository.findOne(id);
    if (jsonCompareEntry == null)
      throw new ResourceNotFoundException();

    return jsonCompareEntry;
  }

  /**
   * Compares the payloads of a JsonComareEntry and returns a
   * JsonDiff with the diff result.
   * @param id - JsonCompareEntry id.
   * @return
   */
  public JsonDiff<String,Object> comparePayloads(long id) {
    JsonCompareEntry jsonCompareEntry = findById(id);
    JsonPayload payloadLeft = null;
    JsonPayload payloadRight = null;

    try {
      payloadLeft = jsonCompareEntry.getJsonPayload(PayloadPosition.LEFT);
      payloadRight = jsonCompareEntry.getJsonPayload(PayloadPosition.RIGHT);
    } catch(NoSuchElementException ex) {
      throw new PayloadComparisonException("You MUST have two payloads defined to perform the comparison.", ex);
    }

    return jsonComparator.compare(payloadLeft.getPayload(), payloadRight.getPayload());
  }

  /**
   * Saves or update a new JsonCompareEntry
   * @param jsonCompareEntry
   * @return
   */
  @Transactional
  public JsonCompareEntry save(JsonCompareEntry jsonCompareEntry) {
    return jsonCompareEntryRepository.save(jsonCompareEntry);
  }

  /**
   * Adds a new JsonPayload to a JsonCompareEntry
   * @param id - JsonCompareEntry id
   * @param jsonPayload - New Payload
   * @return
   */
  @Transactional
  public JsonCompareEntry addPayload(long id, JsonPayload jsonPayload) {
    JsonCompareEntry jsonCompareEntry = findById(id);

    jsonCompareEntry.addPayload(jsonPayload);
    return jsonCompareEntryRepository.save(jsonCompareEntry);
  }
}
