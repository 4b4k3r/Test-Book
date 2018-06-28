package com.ks.FilterEvaluation;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class FilterModel
{
    private static final Hashtable<String, Set<FilterModel>> FILTER_HASH = new Hashtable<>();

    private String id;
    private String port;
    private String idField;
    private String connector;
    private String operator;
    private String parenthesis;
    private String value;
    private String type;

    private Boolean isNegated;

    public FilterModel()
    {
        port = "";
        idField = "";
        id = "";
        connector = "";
        operator = "";
        parenthesis = "";
        parenthesis = "";
        value = "";
        value = type;
        isNegated = false;
    }

    public synchronized void save()
    {
        final Set<FilterModel> filterModels;
        filterModels = FILTER_HASH.contains(this.port) ? FILTER_HASH.get(this.port) : new HashSet<>();
        filterModels.add(this);
        FILTER_HASH.put(this.port, filterModels);
    }

    public static Set<FilterModel> getPortFilters(String port)
    {
        return FILTER_HASH.getOrDefault(port, new HashSet<>());
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getIdField()
    {
        return idField;
    }

    public void setIdField(String idField)
    {
        this.idField = idField;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getConnector()
    {
        return connector;
    }

    public void setConnector(String connector)
    {
        this.connector = connector;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getParenthesis()
    {
        return parenthesis;
    }

    public void setParenthesis(String parenthesis)
    {
        this.parenthesis = parenthesis;
    }

    public String getValue()
    {
        return value;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Boolean getIsNegated()
    {
        return isNegated;
    }

    public void setIsNegated(Boolean isNegated)
    {
        this.isNegated = isNegated;
    }
}
