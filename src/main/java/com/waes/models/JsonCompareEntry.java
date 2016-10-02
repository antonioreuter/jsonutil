package com.waes.models;

import com.fasterxml.jackson.annotation.*;
import com.waes.exceptions.InvalidPayloadPositionException;
import com.waes.exceptions.PayloadExceedMaxNumberException;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aandra1 on 30/09/16.
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonCompareEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JsonPayload> payloads;

  /**
   * Adds a new Json payload.
   *
   * @param payload
   */
  public void addPayload(JsonPayload payload) {
      validatePayloadEntry(payload);

      if (CollectionUtils.isEmpty(payloads))
        payloads = new ArrayList<>();

      payload.setJsonCompareEntry(this);
      payloads.add(payload);

    }

  /**
   * Gets a Json payload based on it's position.
   * @param position
   * @return
   */
    public JsonPayload getJsonPayload(PayloadPosition position) {
      if (CollectionUtils.isEmpty(payloads))
        throw new IllegalStateException("There is no payload registred!");

      return payloads.stream().filter(p -> p.getPosition().equals(position)).findFirst().get();
    }

    private void validatePayloadEntry(JsonPayload payload) {
      if (payload == null)
        throw new IllegalArgumentException("You need to specify a json payload");

      if (!CollectionUtils.isEmpty(payloads)) {
        if (payloads.size() < 2) {
          JsonPayload previous = payloads.get(0);
          if (previous.getPosition().equals(payload.getPosition())) {
            throw new InvalidPayloadPositionException("There is another payload registred to this same position: " + payload.getPosition());
          }
        } else {
          throw new PayloadExceedMaxNumberException("A json comparison entry cannot have more than 2 payloads");
        }
      }
    }
}
