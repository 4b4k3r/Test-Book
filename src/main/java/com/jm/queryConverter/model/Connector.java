package com.jm.queryConverter.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Connector {
  
  AND("AND"),
  OR("OR");

  private String value;

  Connector(String value) {
    this.value = value;
  }

  @JsonCreator
  public static Connector fromValue(String text) {
    for (Connector b : Connector.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

