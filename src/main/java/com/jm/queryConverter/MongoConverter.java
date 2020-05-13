package com.jm.queryConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.queryConverter.model.Connector;
import com.jm.queryConverter.model.Filter;
import com.jm.queryConverter.model.Operator;
import com.jm.queryConverter.model.Parenthesis;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MongoConverter
{
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception
    {
        String plainFilters = "[{\"operador\":\"NOT\",\"parenthesis\":\"(\"},{\"connector\":\"OR\",\"field\":\"Monto1\",\"operador\":\"=\",\"value\":\"\"},{\"connector\":\"AND\",\"field\":\"Monto1\",\"operador\":\"=\",\"value\":\"5000\",\"parenthesis\":\")\"},{\"denied\":\"true\",\"field\":\"Fiid_Tarj\",\"operador\":\"=\",\"value\":\"BCMR\"}]";
        String plainFilters2 = "[{\"operador\":\"NOT\",\"parenthesis\":\"(\"},{\"connector\":\"AND\",\"field\":\"Monto1\",\"operador\":\"=\",\"value\":\"5000\",\"parenthesis\":\")\"},{\"denied\":\"true\",\"field\":\"Fiid_Tarj\",\"operador\":\"=\",\"value\":\"BCMR\"}]";
        String plainFilters3 = "[{\"operador\":\"NOT\",\"parenthesis\":\"(\"},{\"connector\":\"AND\",\"field\":\"Monto1\",\"operador\":\"=\",\"value\":\"5000\"},{\"denied\":\"true\",\"field\":\"Fiid_Tarj\",\"operador\":\"=\",\"value\":\"BCMR\",\"parenthesis\":\")\"}]";
        String plainFilters4 = "[{\"connector\":\"AND\",\"field\":\"Monto1\",\"operador\":\"=\",\"value\":\"5000\"},{\"denied\":\"true\",\"field\":\"Fiid_Tarj\",\"operador\":\"=\",\"value\":\"BCMR\",\"parenthesis\":\")\"}]";
        String plainFilters5 = "[{\"field\":\"Ln_Tarj\",\"operador\":\"NOT LIKE\",\"value\":\"PRO%\",\"connector\":\"AND\",\"parenthesis\":\"\"},{\"field\":\"Tipo\",\"operador\":\"=\",\"value\":\"0210\",\"connector\":\"\",\"parenthesis\":\"\"}]";
        String plainFilters6 = "[{\"field\":\"Origen_Liver\", \"operador\":\"=\",\"value\":\"01\",\"connector\":\"\"}]";
        String plainFilters7 = "[{\"connector\":\"AND\",\"field\":\"time\",\"operador\":\">=\",\"parenthesis\":\"\",\"value\":\"\\\"00:00\\\"\"},{\"connector\":\"\",\"field\":\"time\",\"operador\":\"<=\",\"parenthesis\":\"\",\"value\":\"\\\"23:59\\\"\"}]";
        String plainFilters8 = "[{\"connector\":\"AND\",\"field\":\"time\",\"operador\":\">=\",\"parenthesis\":\"\",\"value\":\"\\\"00:00\\\"\"},{\"connector\":\"\",\"field\":\"time\",\"operador\":\"<=\",\"parenthesis\":\"(\",\"value\":\"\\\"23:59\\\"\"},{\"connector\":\"OR\",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"2\"},{\"connector\":\" \",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"3\"}]";
        String plainFilters9 = "[{\"connector\":\"AND\",\"field\":\"time\",\"operador\":\">=\",\"parenthesis\":\"\",\"value\":\"\\\"00:00\\\"\"},{\"connector\":\"\",\"field\":\"time\",\"operador\":\"<=\",\"parenthesis\":\"(\",\"value\":\"\\\"23:59\\\"\"},{\"connector\":\"OR\",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"2\"},{\"connector\":\"\",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"3\"},{\"connector\":\" \",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"4\"}]";
        String plainFilters10 = "[{\"connector\":\"AND\",\"field\":\"time\",\"operador\":\">=\",\"parenthesis\":\"\",\"value\":\"\\\"00:00\\\"\"},{\"connector\":\"\",\"field\":\"time\",\"operador\":\"<=\",\"parenthesis\":\"(\",\"value\":\"\\\"14:10\\\"\"},{\"connector\":\"OR\",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\" \",\"value\":\"2\"},{\"connector\":\"\",\"field\":\"porcentual_filter_id\",\"operador\":\"=\",\"parenthesis\":\")\",\"value\":\"3\"}]";

        System.out.println(forConditional(getAsList(plainFilters)));
        System.out.println(forConditional(getAsList(plainFilters2)));
        System.out.println(forConditional(getAsList(plainFilters3)));
        System.out.println(forConditional(getAsList(plainFilters4)));
        System.out.println(forConditional(getAsList(plainFilters5)));
        System.out.println(forConditional(getAsList(plainFilters6)));
        System.out.println(forConditional(getAsList(plainFilters7)));
        System.out.println(forConditional(getAsList(plainFilters8)));
        System.out.println(forConditional(getAsList(plainFilters9)));
        System.out.println(forConditional(getAsList(plainFilters10)));
    }

    private static List<Filter> getAsList(String plainFilters) throws IOException
    {
        return Arrays.asList(mapper.readValue(plainFilters, Filter[].class));
    }

    public static String forConditional(List<Filter> filters)
    {
        String query = "";
        Integer innerLevel = 0;

        for (Filter filter : filters)
        {
            if (filter.getOperador() != null && filter.getOperador().equals(Operator.NOT))
            {
                query = splitInOut(query, filter, innerLevel, null);
                innerLevel++;
                continue;
            }

            if (filter.getConnector() != null)
            {
                if (filter.getParenthesis() != null && filter.getParenthesis().equals(Parenthesis.RIGHT_PARENTHESIS))
                {
                    query = splitInOut(query, filter, innerLevel, true);
                    innerLevel--;
                    continue;
                }

                query = splitInOut(query, filter, innerLevel, false);
                innerLevel++;
            }
            else
            {
                query = splitInOut(query, filter, innerLevel, null);
            }
        }

        return cleanQuery(query);
    }

    private static String splitInOut(String query, Filter filter, Integer level, Boolean connectorOutside)
    {
        String concatC = ",";
        List<String> innerSentence = Arrays.asList(query.split("(?=])"));
        level = innerSentence.size() - (level > 0 ? level : 1);

        if (connectorOutside != null)
        {
            if (connectorOutside)
            {
                query = getSequence(innerSentence, level, true) + concatC + getOperatorAndValue(filter) + getSequence(innerSentence, level, false);
                return new Document(getConnector(filter.getConnector()), Collections.singletonList(Document.parse(cleanQuery(query)))).toJson();
            }
            else
            {
                String innerDocument = new Document(getConnector(filter.getConnector()), Collections.singletonList(Document.parse(getOperatorAndValue(filter)))).toJson();
                return getSequence(innerSentence, level, true) + concatC + innerDocument + getSequence(innerSentence, level, false);
            }
        }

        return getSequence(innerSentence, level, true) + concatC + Document.parse(getOperatorAndValue(filter)).toJson() + getSequence(innerSentence, level, false);
    }

    private static String cleanQuery(String query)
    {
        return (query.startsWith(",{") ? query.substring(1) : query).replace("[,{","[{");
    }

    private static String getSequence(List<String> innerSentence, Integer level, boolean isFirst)
    {
        return innerSentence.stream().skip(isFirst ? 0 : level).limit(isFirst ? level : innerSentence.size()).collect(Collectors.joining());
    }

    private static String getOperatorAndValue(Filter filter)
    {
        if (filter.getValue() == null)
        {
            return new Document(getOperator(filter), new LinkedList<>()).toJson();
        }

        final String field = filter.getField();
        String value = filter.getValue();
        final Operator operator = filter.getDenied() ? filter.getOperador().getOpposite() : filter.getOperador();

        if (!value.contains("\""))
        {
            if (!field.equals("porcentual_filter_id") && !field.equals("value") && !field.equals("value2"))
            {
                if (!operator.equals(Operator.NOT_LIKE) && !operator.equals(Operator.LIKE))
                {
                    value = "\"" + value + "\"";
                }
            }
        }

        switch (operator)
        {
            case GREATER_THAN_OR_EQUAL_TO:
                return "{ " + field + ": " + "{ $gte: " + value + "}" + " }";
            case LESS_THAN_OR_EQUAL_TO:
                return "{ " + field + ": " + "{ $lte: " + value + "}" + " }";
            case GREATER_THAN:
                return "{ " + field + ": " + "{ $gt: " + value + "}" + " }";
            case LESS_THAN:
                return "{ " + field + ": " + "{ $lt: " + value + "}" + " }";
            case NOT_EQUAL:
                return "{ " + field + ": " + "{ $ne : " + value + "}" + " }";
            case NOT_LIKE:
                return "{ " + field + ": " + "{ $ne : " + "{ $regex: \"" + ".*(?<!" + value.replace("%", "") + ")$" + "\"}" + "}" + " }";
            case EQUAL:
                return "{ " + field + ": " + "{ $eq: " + value + "}" + " }";
            case LIKE:
                return "{ " + field + ": " + (value.contains("%") ? "{ $regex: \"" + value.replace("%", "") + "\"}" : "{ $eq: \"" + value + "\"}") + " }";
            default:
                return null;
        }
    }

    private static String getOperator(Filter filter)
    {
        switch (filter.getOperador())
        {
            case NOT:
                return "$nor";
            case GREATER_THAN_OR_EQUAL_TO:
                return "$gte";
            case LESS_THAN_OR_EQUAL_TO:
                return "$lte";
            case GREATER_THAN:
                return "$gt";
            case LESS_THAN:
                return "$lt";
            case NOT_EQUAL:
                return "$ne ";
            case NOT_LIKE:
                return "$regex";
            case EQUAL:
            case LIKE:
                return filter.getValue().contains("%") ? "$regex" : "$eq";
            default:
                return null;
        }
    }

    private static String getConnector(Connector connector)
    {
        switch (connector)
        {
            case OR:
                return "$or";
            case AND:
                return "$and";
            default:
                return null;
        }
    }
}
