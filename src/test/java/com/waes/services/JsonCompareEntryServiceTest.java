package com.waes.services;

import com.waes.comparators.JsonComparator;
import com.waes.exceptions.ResourceNotFoundException;
import com.waes.models.JsonCompareEntry;
import com.waes.repositories.JsonCompareEntryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by aandra1 on 01/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonCompareEntryServiceTest {

  @Mock
  JsonComparator jsonComparator;

  @Mock
  JsonCompareEntryRepository jsonCompareEntryRepository;

  @Spy
  @InjectMocks
  JsonCompareEntryService subject;

  @Test(expected = ResourceNotFoundException.class)
  public void whenThereIsNoJsonCompareEntry() {
    when(jsonCompareEntryRepository.findOne(any(Long.class))).thenReturn(null);

    subject.findById(1L);
  }

  @Test
  public void whenFoundAJsonCompareEntry() {
    JsonCompareEntry data = JsonCompareEntry.builder().id(1L).name("Test 01").build();

    when(jsonCompareEntryRepository.findOne(any(Long.class))).thenReturn(data);

    JsonCompareEntry jsonCompareEntry = subject.findById(1L);
    assertEquals(data, jsonCompareEntry);
  }
}
