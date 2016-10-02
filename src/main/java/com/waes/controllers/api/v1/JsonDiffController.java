package com.waes.controllers.api.v1;


import com.waes.models.JsonCompareEntry;
import com.waes.models.JsonDiff;
import com.waes.models.JsonPayload;
import com.waes.models.PayloadPosition;
import com.waes.services.JsonCompareEntryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by aandra1 on 30/09/16.
 */

@Slf4j
@RequestMapping("/api/v1/diff")
@RestController("jsonDiffController")
public class JsonDiffController {

    @Autowired
    private JsonCompareEntryService jsonCompareEntryService;


  /**
   * Creates an entry to group the payloads that you want to compare.
   *
   * @param jsonCompareEntry
   * @return
   */
  @ApiOperation(value = "Creates an entry to group the payloads that you want to compare.", notes = "Save a new Json entry.")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public JsonCompareEntry createEntry(@RequestBody JsonCompareEntry jsonCompareEntry) {
        log.info("Creating a new: {}", jsonCompareEntry);
        return jsonCompareEntryService.save(jsonCompareEntry);
    }

  /**
   *Saves the Json payload in the left side on a Json Entry already created.
   *
   * @param id
   * @param payload
   * @return
   */
    @ApiOperation(value = "Saves the Json payload in the left side on a Json Entry already created.", notes = "Adds a new Json payload in the left side.")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/left", method = RequestMethod.POST)
    public JsonPayload leftJson(@PathVariable("id") long id, @RequestBody String payload) {
        return saveJsonPayload(id, PayloadPosition.LEFT, payload);
    }

  /**
   *
   * @param id
   * @param payload
   * @return
   */
    @ApiOperation(value = "Adds the right Json payload that you want to compare", notes = "Saves a new Right Json payload.")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/{id}/right", method = RequestMethod.POST)
    public JsonPayload rightJson(@PathVariable("id") long id, @RequestBody String payload) {
      return saveJsonPayload(id, PayloadPosition.RIGHT, payload);
    }

  /**
   *
   * @param id
   * @return
   */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonDiff<String,Object> compareJson(@PathVariable("id") long id) {
        return jsonCompareEntryService.comparePayloads(id);
    }

    private JsonPayload saveJsonPayload(long id, PayloadPosition position, String payload) {
      log.info("Payload ({}): {} {}", id, position, payload);

      JsonPayload jsonPayload = JsonPayload.builder().payload(payload).position(position).build();
      JsonCompareEntry jsonCompareEntry = jsonCompareEntryService.addPayload(id, jsonPayload);
      return jsonCompareEntry.getJsonPayload(position);
    }
}
