package org.romainlavabre.pagination.condition;

import org.junit.Assert;
import org.junit.Test;
import org.romainlavabre.pagination.exception.NotSupportedKey;
import org.romainlavabre.pagination.exception.NotSupportedOperator;
import org.romainlavabre.pagination.exception.NotSupportedValue;
import org.romainlavabre.request.MockRequest;
import org.romainlavabre.request.Request;

import java.util.List;
import java.util.Map;

public class ConditionTest {

    @Test
    public void testEq() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[eq]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 = :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testNe() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[ne]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 != :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testSup() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[sup]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 > :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testInf() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[inf]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 < :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testSupEq() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[supeq]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 >= :key1000", conditions.get( 0 ).consume( 1, null ) );
    }


    @Test
    public void testInfEq() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[infeq]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 <= :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testContains() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[contains]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 LIKE :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testNeContains() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[necontains]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 NOT LIKE :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testStartWith() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[startwith]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 LIKE :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testEndWith() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[endwith]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "column_1 LIKE :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testJsonContains() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[jsoncontains]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "JSON_CONTAINS(column_1,:key1000)", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testNeJsonContains() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[nejsoncontains]", "value1"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "!JSON_CONTAINS(column_1,:key1000)", conditions.get( 0 ).consume( 1, null ) );
    }


    @Test
    public void testEqAndNe() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[eq]", "value1",
                "column_2[ne]", "value2"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(2, conditions.size());
        Assert.assertEquals( "column_1 = :key1000", conditions.get( 1 ).consume( 1, null ) );
        Assert.assertEquals( "column_2 != :key1000", conditions.get( 0 ).consume( 1, null ) );
    }


    @Test
    public void testDistanceSup() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[distancesup]", "3.563467,-5.563467;300"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "ST_DISTANCE_SPHERE(column_1, POINT(3.563467,-5.563467)) > :key1000", conditions.get( 0 ).consume( 1, null ) );
    }

    @Test
    public void testDistanceInf() throws NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "column_1[distanceinf]", "3.563467,-5.563467;300"
        ) );

        List<Condition> conditions = ConditionBuilder.getConditions( request );

        Assert.assertEquals(1, conditions.size());
        Assert.assertEquals( "ST_DISTANCE_SPHERE(column_1, POINT(3.563467,-5.563467)) < :key1000", conditions.get( 0 ).consume( 1, null ) );
    }
}
