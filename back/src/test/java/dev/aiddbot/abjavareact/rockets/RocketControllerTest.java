package dev.aiddbot.abjavareact.rockets;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RocketController.class)
class RocketControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private RocketService service;

  @Test
  void createsRocket() throws Exception {
    RocketResponse created = new RocketResponse("id-1", "Aurora", 4, RocketRange.MOON, false);
    given(service.create(new RocketRequest("Aurora", 4, RocketRange.MOON))).willReturn(created);

    mvc.perform(post("/api/rockets")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":"Aurora","capacity":4,"range":"Moon"}
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("id-1"))
        .andExpect(jsonPath("$.name").value("Aurora"))
        .andExpect(jsonPath("$.capacity").value(4))
        .andExpect(jsonPath("$.range").value("Moon"))
        .andExpect(jsonPath("$.decommissioned").value(false));
  }

  @Test
  void listsCatalog() throws Exception {
    given(service.list()).willReturn(List.of(
        new RocketResponse("id-1", "Aurora", 4, RocketRange.MOON, false),
        new RocketResponse("id-2", "Atlas", 6, RocketRange.MARS, true)
    ));

    mvc.perform(get("/api/rockets"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Aurora"))
        .andExpect(jsonPath("$[1].decommissioned").value(true));
  }

  @Test
  void rejectsInvalidPayload() throws Exception {
    mvc.perform(post("/api/rockets")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":" ","capacity":99,"range":"Pluto"}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void returnsNotFoundForUnknownRocket() throws Exception {
    given(service.getById("missing")).willThrow(new RocketNotFoundException("missing"));

    mvc.perform(get("/api/rockets/missing"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Rocket not found: missing"));
  }

  @Test
  void decommissionsRocket() throws Exception {
    mvc.perform(delete("/api/rockets/id-1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void updatesRocket() throws Exception {
    RocketResponse updated = new RocketResponse("id-1", "Nova", 8, RocketRange.MARS, false);
    given(service.update("id-1", new RocketRequest("Nova", 8, RocketRange.MARS))).willReturn(updated);

    mvc.perform(put("/api/rockets/id-1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":"Nova","capacity":8,"range":"Mars"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Nova"))
        .andExpect(jsonPath("$.capacity").value(8))
        .andExpect(jsonPath("$.range").value("Mars"));
  }
}
