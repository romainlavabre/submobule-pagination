package org.romainlavabre.pagination.query;

import org.romainlavabre.pagination.annotation.Pagination;
import org.romainlavabre.pagination.condition.Condition;
import org.romainlavabre.pagination.exception.FileError;
import org.romainlavabre.pagination.exception.NotSupportedKey;
import org.romainlavabre.pagination.exception.NotSupportedOperator;
import org.romainlavabre.pagination.exception.NotSupportedValue;
import org.romainlavabre.pagination.query.file.QueryFileParser;
import org.romainlavabre.request.Request;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FileMode {
    protected final Map< String, String > FILE_CACHE = new HashMap<>();
    protected final ResourceLoader  resourceLoader;
    protected final QueryFileParser queryFileParser;


    public FileMode( ResourceLoader resourceLoader, QueryFileParser queryFileParser ) {
        this.resourceLoader  = resourceLoader;
        this.queryFileParser = queryFileParser;
    }


    public Query get( Request request, final List< Condition > conditions, Pagination pagination ) throws NotSupportedValue, NotSupportedKey, NotSupportedOperator, FileError {
        String fileContent = getFileContent( pagination.filePath() );
        Query  query       = new Query();

        QueryFileParser.ParsingResult parsingResult = queryFileParser.getFilter( pagination.filePath(), fileContent );

        StringBuilder conditionStr = new StringBuilder();
        String mode = request.getQueryString( "mode" );
        String keyword = !"include".equals( mode ) ? "AND" : "OR";

        for ( int i = 0; i < conditions.size(); i++ ) {
            final Condition condition = conditions.get( i );

            if ( i == 0 ) {
                conditionStr.append( " " );
            } else {


                conditionStr.append( " " + keyword + " " );
            }

            conditionStr.append( condition.consume( i + 1, parsingResult.getFilters().get( condition.getKey() ), keyword) );

            for ( final Map.Entry< String, String > entry : condition.getParameters().entrySet() ) {
                query.addParameter( entry.getKey(), entry.getValue() );
            }
        }

        fileContent = queryFileParser.putCondition( conditionStr.toString(), fileContent );
        String countQuery = queryFileParser.toCountQuery( pagination.filePath(), fileContent, parsingResult.getFilters().get( "id" ) );

        query.setCountQuery( countQuery );

        final String sortBy  = request.getQueryString( "sortBy" ) == null ? request.getQueryString( "sort_by" ) : request.getQueryString( "sortBy" );
        final String orderBy = request.getQueryString( "orderBy" ) == null ? request.getQueryString( "order_by" ) : request.getQueryString( "orderBy" );
        final String limit   = request.getQueryString( "per_page" );

        if ( !limit.matches( "[0-9]+" ) ) {
            throw new NotSupportedValue( "per_page", limit );
        }

        final int offset = Integer.parseInt( request.getQueryString( "per_page" ) ) * ( Integer.parseInt( request.getQueryString( "page" ) ) - 1 );

        if ( pagination.allowSorting() && !Objects.equals( sortBy, "NONE" ) ) {
            fileContent = queryFileParser.putSort( parsingResult.getFilters().get( sortBy.replace( " ", "" ) ), orderBy, fileContent );
        }

        fileContent = queryFileParser.putLimitAndOffset( Integer.parseInt( limit ), offset, fileContent );

        query.setOffset( offset );
        query.setLimit( Integer.parseInt( limit ) );
        query.setDataQuery( fileContent );

        return query;
    }


    private String getFileContent( String filePath ) throws FileError {
        if ( FILE_CACHE.containsKey( filePath ) ) {
            return FILE_CACHE.get( filePath );
        }

        Resource resource = resourceLoader.getResource( filePath );

        String fileContent;

        try {
            StringBuilder textBuilder = new StringBuilder();
            try ( Reader reader = new BufferedReader( new InputStreamReader( resource.getInputStream(), Charset.forName( StandardCharsets.UTF_8.name() ) ) ) ) {
                int c;

                while ( ( c = reader.read() ) != -1 ) {
                    textBuilder.append( ( char ) c );
                }
            }

            fileContent = textBuilder.toString();
        } catch ( IOException e ) {
            e.printStackTrace();
            throw new FileError( filePath );
        }

        FILE_CACHE.put( filePath, fileContent );

        return fileContent;
    }
}
