package dev.aiddbot.abjavareact.bookings;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
  List<Booking> findByLaunchId(String launchId);
  Optional<Booking> findByIdAndLaunchId(String id, String launchId);
}
