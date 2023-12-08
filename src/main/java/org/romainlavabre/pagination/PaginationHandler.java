package org.romainlavabre.pagination;


import org.romainlavabre.pagination.exception.*;
import org.romainlavabre.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PaginationHandler {
    /**
     * @param request
     * @param dtoType Data model class
     * @param <T>
     * @return Pagination object to encode
     * @throws NotSupportedOperator If an operator is invalid
     * @throws NotSupportedKey      If a key is invalid (Prevent SQL injection)
     * @throws NotSupportedValue    If a value is invalid (Prevent SQL injection)
     */
    < T > Pagination getResult( Request request, Class< T > dtoType )
            throws NotSupportedOperator, NotSupportedKey, NotSupportedValue, NotSupportedDtoType, FileError;
}
