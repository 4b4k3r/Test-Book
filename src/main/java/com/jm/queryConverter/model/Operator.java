package com.jm.queryConverter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum Operator
{

    NOT("NOT"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    GREATER_THAN_OR_EQUAL_TO(">=");

    private String value;

    Operator(String value)
    {
        this.value = value;
    }

    @JsonCreator
    public static Operator fromValue(String text)
    {
        for (Operator b : Operator.values())
        {
            if (String.valueOf(b.value).equals(text))
            {
                return b;
            }
        }
        return null;
    }

    public Operator getOpposite()
    {
        switch (this)
        {
            case LIKE:
                return NOT_LIKE;
            case NOT_LIKE:
                return LIKE;
            case EQUAL:
                return NOT_EQUAL;
            case NOT_EQUAL:
                return EQUAL;
            case GREATER_THAN:
                return LESS_THAN_OR_EQUAL_TO;
            case LESS_THAN:
                return GREATER_THAN_OR_EQUAL_TO;
            case LESS_THAN_OR_EQUAL_TO:
                return GREATER_THAN;
            case GREATER_THAN_OR_EQUAL_TO:
                return LESS_THAN;
            default:
                log.error("Unable to find opposite for: " + this.toString());
                return this;
        }
    }
}

