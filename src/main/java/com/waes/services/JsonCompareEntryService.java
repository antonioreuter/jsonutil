package com.waes.services;

import com.waes.comparators.JsonComparator;
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

/**
 * Created by aandra1 on 30/09/16.
 */
@Service("jsonCompareEntryService")
public class JsonCompareEntryService {

  @Qualifier(value = "jsonComparator")
  @Autowired
  private JsonComparator jsonComparator;

  @Autowired
  private JsonCompareEntryRepository jsonCompareEntryRepository;

  public JsonCompareEntry findById(long id) {
    JsonCompareEntry jsonCompareEntry = jsonCompareEntryRepository.findOne(id);
    if (jsonCompareEntry == null)
      throw new ResourceNotFoundException();

    return jsonCompareEntry;
  }

  public JsonDiff<String,Object> comparePayloads(long id) {
    JsonCompareEntry jsonCompareEntry = findById(id);
    JsonPayload payloadLeft = jsonCompareEntry.getJsonPayload(PayloadPosition.LEFT);
    JsonPayload payloadRight = jsonCompareEntry.getJsonPayload(PayloadPosition.RIGHT);

    return jsonComparator.compare(payloadLeft.getPayload(), payloadRight.getPayload());
  }

  @Transactional
  public JsonCompareEntry save(JsonCompareEntry jsonCompareEntry) {
    return jsonCompareEntryRepository.save(jsonCompareEntry);
  }

  @Transactional
  public JsonCompareEntry addPayload(long id, JsonPayload jsonPayload) {
    JsonCompareEntry jsonCompareEntry = findById(id);

    jsonCompareEntry.addPayload(jsonPayload);
    return jsonCompareEntryRepository.save(jsonCompareEntry);
  }
}
