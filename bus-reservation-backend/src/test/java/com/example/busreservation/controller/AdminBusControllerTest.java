package com.example.busreservation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.busreservation.entity.Bus;
import com.example.busreservation.repository.BusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

class AdminBusControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BusRepository busRepo;

    @InjectMocks
    private AdminBusController controller;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAllBuses() throws Exception {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("BUS123");
        when(busRepo.findAll()).thenReturn(List.of(bus));

        mockMvc.perform(get("/api/v1/admin/buses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].busNumber").value("BUS123"));
    }

    @Test
    void testCreateBus() throws Exception {
        Bus bus = new Bus();
        bus.setBusNumber("BUS123");
        when(busRepo.save(any(Bus.class))).thenAnswer(i -> {
            Bus b = i.getArgument(0);
            b.setId(1L);
            return b;
        });

        mockMvc.perform(post("/api/v1/admin/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bus)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.busNumber").value("BUS123"));
    }

    @Test
    void testGetBus_Found() throws Exception {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("BUS123");
        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));

        mockMvc.perform(get("/api/v1/admin/buses/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.busNumber").value("BUS123"));
    }

    @Test
    void testGetBus_NotFound() throws Exception {
        when(busRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/admin/buses/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBus_Success() throws Exception {
        when(busRepo.existsById(1L)).thenReturn(true);
        doNothing().when(busRepo).deleteById(1L);

        mockMvc.perform(delete("/api/v1/admin/buses/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBus_NotFound() throws Exception {
        when(busRepo.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/admin/buses/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBus_Success() throws Exception {
        Bus existing = new Bus();
        existing.setId(1L);
        existing.setBusNumber("OLD");

        Bus updated = new Bus();
        updated.setBusNumber("NEW");

        when(busRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(busRepo.save(any(Bus.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/api/v1/admin/buses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.busNumber").value("NEW"));
    }

    @Test
    void testUpdateBus_NotFound() throws Exception {
        Bus updated = new Bus();
        updated.setBusNumber("NEW");

        when(busRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/admin/buses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated)))
            .andExpect(status().isNotFound());
    }
}

