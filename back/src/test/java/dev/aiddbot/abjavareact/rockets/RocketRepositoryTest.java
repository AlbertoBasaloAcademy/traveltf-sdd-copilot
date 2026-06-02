package dev.aiddbot.abjavareact.rockets;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RocketRepositoryTest {

  @Autowired
  private RocketRepository repository;

  @Test
  void persistsAndReadsRocket() {
    Rocket saved = repository.save(new Rocket("Aurora", 4, RocketRange.MOON));

    assertThat(saved.getId()).isNotBlank();
    assertThat(repository.findById(saved.getId()))
        .isPresent()
        .get()
        .satisfies(found -> {
          assertThat(found.getName()).isEqualTo("Aurora");
          assertThat(found.getCapacity()).isEqualTo(4);
          assertThat(found.getRange()).isEqualTo(RocketRange.MOON);
          assertThat(found.isDecommissioned()).isFalse();
        });
  }
}
