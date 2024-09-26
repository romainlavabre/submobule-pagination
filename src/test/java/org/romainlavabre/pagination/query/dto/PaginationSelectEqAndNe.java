package org.romainlavabre.pagination.query.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;

@Pagination(mode = ModeType.FILE, filePath = "src/test/resources/query/select-eq-and-ne.sql")
@Entity
public class PaginationSelectEqAndNe {
    @Id
    private long column_1;

    private long column_2;
}
