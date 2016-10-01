package com.waes.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by aandra1 on 30/09/16.
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "payload", "position"})
@ToString(of = {"id", "position"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonPayload implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter(AccessLevel.NONE)
    @NotNull
    private String payload;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PayloadPosition position;

    @ManyToOne
    private JsonCompareEntry jsonCompareEntry;

    @JsonIgnore
    public String getPayload() {
        return this.payload;
    }
}
