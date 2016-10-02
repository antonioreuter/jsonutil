package com.waes.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by aandra1 on 30/09/16.
 */

@ToString(of = {"description"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PayloadPosition {

    LEFT("Left"), RIGHT("Right");

    @Getter
    @Setter(AccessLevel.NONE)
    private String description;
}
