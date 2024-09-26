package org.romainlavabre.pagination.query;

import org.junit.Assert;
import org.junit.Test;
import org.romainlavabre.pagination.condition.Condition;
import org.romainlavabre.pagination.condition.ConditionBuilder;
import org.romainlavabre.pagination.exception.*;
import org.romainlavabre.pagination.query.dto.PaginationSelectEq;
import org.romainlavabre.pagination.query.dto.PaginationSelectEqAndNe;
import org.romainlavabre.pagination.query.dto.PaginationSelectWithFunction;
import org.romainlavabre.pagination.query.dto.PaginationSelectWithSubQuery;
import org.romainlavabre.pagination.query.file.QueryFileParser;
import org.romainlavabre.request.MockRequest;
import org.romainlavabre.request.Request;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class QueryTest {


    @Test
    public void testEq() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC",
                "column_1[eq]", "value1"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectEq.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE  column_1 = :key1000""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1,
                        FROM table_1
                        WHERE  column_1 = :key1000
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }

    @Test
    public void testEqAndNe() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC",
                "column_1[eq]", "value1",
                "column_2[ne]", "value2"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectEqAndNe.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE  column_2 != :key1000 AND column_1 = :key2000""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1,
                        column_2 AS column_2
                        FROM table_1
                        WHERE  column_2 != :key1000 AND column_1 = :key2000
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }

    @Test
    public void testWithSubQuery() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC",
                "column_1[eq]", "value1",
                "column_2[ne]", "value2"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectWithSubQuery.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE  (
                            SELECT T2.sub_col_1 FROM table_2 T2 WHERE T2.foreign_key LIKE "match"
                        ) != :key1000 AND column_1 = :key2000""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1,
                        (
                            SELECT T2.sub_col_1 FROM table_2 T2 WHERE T2.foreign_key LIKE "match"
                        ) AS column_2
                        FROM table_1
                        WHERE  (
                            SELECT T2.sub_col_1 FROM table_2 T2 WHERE T2.foreign_key LIKE "match"
                        ) != :key1000 AND column_1 = :key2000
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }

    @Test
    public void testWithFunction() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC",
                "column_1[jsoncontains]", "value1"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectWithFunction.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE  JSON_CONTAINS(column_1,:key1000)""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1
                        FROM table_1
                        WHERE  JSON_CONTAINS(column_1,:key1000)
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }


    @Test
    public void testWithoutCondition() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectEq.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE 1""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1,
                        FROM table_1
                        WHERE 1
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }


    @Test
    public void testWithNullValue() throws NotSupportedKey, NotSupportedOperator, FileError, NotSupportedDtoType, NotSupportedValue {
        Request request = MockRequest.build( Map.of(), Map.of(), Map.of(), Map.of(
                "per_page", "10",
                "page","1",
                "sort_by","column_1",
                "order_by","DESC",
                "column_1[eq]", "null"
        ) );

        List< Condition > conditions = ConditionBuilder.getConditions( request );
        Query query = getQueryBuilder().build( request, conditions, PaginationSelectEq.class );

        Assert.assertEquals(
                """
                        SELECT COUNT(id) FROM table_1
                        WHERE  column_1 IS NULL\s""",
                query.getCountQuery()
        );
        Assert.assertEquals(
                """
                        SELECT
                        id AS id,
                        column_1 AS column_1,
                        FROM table_1
                        WHERE  column_1 IS NULL\s
                        ORDER BY column_1 DESC
                        LIMIT 10 OFFSET 0""",
                query.getDataQuery()
        );
    }

    private QueryBuilder getQueryBuilder() {
        return new QueryBuilder(
                new ViewMode(),
                new FileMode( new ResourceLoader() {
                    @Override
                    public Resource getResource( String location ) {
                        return new Resource() {
                            @Override
                            public boolean exists() {
                                return true;
                            }


                            @Override
                            public URL getURL() throws IOException {
                                return null;
                            }


                            @Override
                            public URI getURI() throws IOException {
                                return null;
                            }


                            @Override
                            public File getFile() throws IOException {
                                return null;
                            }


                            @Override
                            public long contentLength() throws IOException {
                                return 0;
                            }


                            @Override
                            public long lastModified() throws IOException {
                                return 0;
                            }


                            @Override
                            public Resource createRelative( String relativePath ) throws IOException {
                                return null;
                            }


                            @Override
                            public String getFilename() {
                                return "";
                            }


                            @Override
                            public String getDescription() {
                                return "";
                            }


                            @Override
                            public InputStream getInputStream() throws IOException {
                                return Files.newInputStream( Path.of( location ) );
                            }
                        };
                    }


                    @Override
                    public ClassLoader getClassLoader() {
                        return getClass().getClassLoader();
                    }
                }, new QueryFileParser() )
        );
    }
}
