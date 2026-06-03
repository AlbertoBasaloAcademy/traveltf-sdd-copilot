package dev.aiddbot.abjavareact.launches;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchRepository extends JpaRepository<Launch, String> {
  List<Launch> findAllByOrderByLaunchTimeAsc();
}
