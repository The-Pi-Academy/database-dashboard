package org.academy.pi.sql.models;

public enum QueryCategory {
  SELECT_QUERIES("SELECT Queries"),
  INSERT_UPDATE_DELETE("INSERT/UPDATE/DELETE"),
  TABLE_OPERATIONS("Table Operations");

  private final String displayName;

  QueryCategory(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
