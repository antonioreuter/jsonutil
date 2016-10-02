package com.waes.models;

import com.waes.exceptions.InvalidPayloadPositionException;
import com.waes.exceptions.PayloadExceedMaxNumberException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by aandra1 on 01/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonCompareEntryTest {

  @Test(expected = IllegalArgumentException.class)
  public void addNullAsPayload() {
    JsonCompareEntry subject = JsonCompareEntry.builder().id(1L).name("Test 1").build();

    subject.addPayload(null);
  }

  @Test(expected = InvalidPayloadPositionException.class)
  public void addTwoPayloadsToSamePosition() {
    JsonCompareEntry subject = JsonCompareEntry.builder().id(1L).name("Test 2").build();

    JsonPayload payloadLeft1 = JsonPayload.builder().id(1L).payload("{}").position(PayloadPosition.LEFT).build();
    JsonPayload payloadLeft2 = JsonPayload.builder().id(2L).payload("{}").position(PayloadPosition.LEFT).build();

    subject.addPayload(payloadLeft1);
    subject.addPayload(payloadLeft2);
  }

  @Test(expected = PayloadExceedMaxNumberException.class)
  public void addAThirdPayload() {
    JsonCompareEntry subject = JsonCompareEntry.builder().id(1L).name("Test 3").build();

    JsonPayload payloadLeft1 = JsonPayload.builder().id(1L).payload("{}").position(PayloadPosition.LEFT).build();
    JsonPayload payloadRight1 = JsonPayload.builder().id(2L).payload("{}").position(PayloadPosition.RIGHT).build();
    JsonPayload payloadLeft2 = JsonPayload.builder().id(3L).payload("{}").position(PayloadPosition.LEFT).build();

    subject.addPayload(payloadLeft1);
    subject.addPayload(payloadRight1);
    subject.addPayload(payloadLeft2);
  }

  @Test(expected = IllegalStateException.class)
  public void tryToGetAPayloadWhenItIsEmpty() {
    JsonCompareEntry subject = JsonCompareEntry.builder().id(1L).name("Test 4").build();

    subject.getJsonPayload(PayloadPosition.LEFT);
  }

  @Test
  public void findThePayloadBasedOnPosition() {
    JsonCompareEntry subject = JsonCompareEntry.builder().id(1L).name("Test 4").build();

    JsonPayload payloadLeft = JsonPayload.builder().id(1L).payload("{}").position(PayloadPosition.LEFT).build();
    JsonPayload payloadRight = JsonPayload.builder().id(2L).payload("{}").position(PayloadPosition.RIGHT).build();
    subject.addPayload(payloadLeft);
    subject.addPayload(payloadRight);

    JsonPayload payload = subject.getJsonPayload(PayloadPosition.RIGHT);

    Assert.assertNotNull(payload);
    Assert.assertEquals(2L, payload.getId().longValue());
    Assert.assertEquals(PayloadPosition.RIGHT, payload.getPosition());
  }
}
