package org.romainlavabre.pagination.query.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;

@Pagination(mode = ModeType.FILE, filePath = "src/test/resources/query/select-with-function.sql")
@Entity
public class PaginationSelectWithFunction {
    @Id
    private long column_1;
}
