package org.romainlavabre.pagination.rt;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface RealTimeJpa extends JpaRepository< RealTime, Long > {
}
