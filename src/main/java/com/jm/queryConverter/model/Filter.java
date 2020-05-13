package com.jm.queryConverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter
{
    @JsonProperty("denied")
    private Boolean denied = false;

    @JsonProperty("field")
    private String field = null;

    @JsonProperty("operador")
    private Operator operador = null;

    @JsonProperty("value")
    private String value = null;

    @JsonProperty("connector")
    private Connector connector = null;

    @JsonProperty("parenthesis")
    private Parenthesis parenthesis = null;

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}

