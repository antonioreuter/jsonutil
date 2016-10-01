package com.waes.repositories;

import com.waes.models.JsonCompareEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by aandra1 on 30/09/16.
 */

@Repository
public interface JsonCompareEntryRepository extends CrudRepository<JsonCompareEntry, Long> {

}
