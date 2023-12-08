package org.romainlavabre.pagination.query;


import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;
import org.romainlavabre.pagination.condition.Condition;
import org.romainlavabre.pagination.exception.*;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "PaginationQueryBuilder" )
public class QueryBuilder {

    protected final ViewMode viewMode;
    protected final FileMode fileMode;


    public QueryBuilder( ViewMode viewMode, FileMode fileMode ) {
        this.viewMode = viewMode;
        this.fileMode = fileMode;
    }


    public Query build( final Request request, final List< Condition > conditions, Class< ? > dtoType )
            throws NotSupportedKey, NotSupportedOperator, NotSupportedValue, NotSupportedDtoType, FileError {
        Pagination pagination = dtoType.getDeclaredAnnotation( Pagination.class );

        if ( pagination == null ) {
            throw new NotSupportedDtoType();
        }

        return pagination.mode() == ModeType.VIEW
                ? viewMode.get( request, conditions, pagination )
                : fileMode.get( request, conditions, pagination );
    }


}
