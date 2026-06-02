package dev.aiddbot.abjavareact.rockets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RocketServiceTest {

  @Mock
  private RocketRepository repository;

  @Test
  void createsRocketFromRequest() {
    Rocket persisted = new Rocket("Aurora", 4, RocketRange.MOON);
    given(repository.save(org.mockito.ArgumentMatchers.any(Rocket.class))).willReturn(persisted);
    RocketService service = new RocketService(repository);

    RocketResponse response = service.create(new RocketRequest(" Aurora ", 4, RocketRange.MOON));

    assertThat(response.name()).isEqualTo("Aurora");
    assertThat(response.capacity()).isEqualTo(4);
    assertThat(response.range()).isEqualTo(RocketRange.MOON);
    assertThat(response.decommissioned()).isFalse();

    ArgumentCaptor<Rocket> captor = ArgumentCaptor.forClass(Rocket.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo("Aurora");
  }

  @Test
  void updatesRocketData() {
    Rocket existing = new Rocket("Old", 2, RocketRange.EARTH);
    given(repository.findById(existing.getId())).willReturn(Optional.of(existing));
    given(repository.save(existing)).willReturn(existing);
    RocketService service = new RocketService(repository);

    RocketResponse response = service.update(existing.getId(), new RocketRequest("New", 7, RocketRange.MARS));

    assertThat(response.name()).isEqualTo("New");
    assertThat(response.capacity()).isEqualTo(7);
    assertThat(response.range()).isEqualTo(RocketRange.MARS);
  }

  @Test
  void marksRocketAsDecommissioned() {
    Rocket existing = new Rocket("Old", 2, RocketRange.EARTH);
    given(repository.findById(existing.getId())).willReturn(Optional.of(existing));
    given(repository.save(existing)).willReturn(existing);
    RocketService service = new RocketService(repository);

    service.decommission(existing.getId());

    assertThat(existing.isDecommissioned()).isTrue();
    verify(repository).save(existing);
  }

  @Test
  void doesNotPersistAgainWhenAlreadyDecommissioned() {
    Rocket existing = new Rocket("Old", 2, RocketRange.EARTH);
    existing.setDecommissioned(true);
    given(repository.findById(existing.getId())).willReturn(Optional.of(existing));
    RocketService service = new RocketService(repository);

    service.decommission(existing.getId());

    verify(repository, never()).save(existing);
  }

  @Test
  void throwsWhenRocketIsMissing() {
    given(repository.findById("missing")).willReturn(Optional.empty());
    RocketService service = new RocketService(repository);

    assertThatThrownBy(() -> service.getById("missing"))
        .isInstanceOf(RocketNotFoundException.class)
        .hasMessageContaining("missing");
  }

  @Test
  void listsCatalog() {
    Rocket one = new Rocket("One", 1, RocketRange.EARTH);
    Rocket two = new Rocket("Two", 3, RocketRange.MARS);
    given(repository.findAll()).willReturn(List.of(one, two));
    RocketService service = new RocketService(repository);

    List<RocketResponse> list = service.list();

    assertThat(list).hasSize(2);
    assertThat(list.get(0).name()).isEqualTo("One");
    assertThat(list.get(1).name()).isEqualTo("Two");
  }
}
