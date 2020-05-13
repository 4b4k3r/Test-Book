package com.jm.queryConverter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Parenthesis {

  RIGHT_PARENTHESIS(")"),
  LEFT_PARENTHESIS("(");

  private String value;

  Parenthesis(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Parenthesis fromValue(String text) {
    for (Parenthesis b : Parenthesis.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

