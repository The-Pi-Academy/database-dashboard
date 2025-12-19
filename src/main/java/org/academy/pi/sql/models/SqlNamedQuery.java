package org.academy.pi.sql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SqlNamedQuery {

  private String title;
  private String query;
  private QueryCategory category;

  @JsonProperty("category")
  public String getCategoryDisplayName() {
    return category != null ? category.getDisplayName() : null;
  }
}