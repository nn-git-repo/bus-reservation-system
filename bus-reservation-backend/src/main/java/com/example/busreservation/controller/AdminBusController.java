package com.example.busreservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Bus;
import com.example.busreservation.repository.BusRepository;

@RestController @RequestMapping("/api/v1/admin/buses")
public class AdminBusController {
    private final BusRepository repo;
    public AdminBusController(BusRepository repo){ this.repo = repo; }

    @GetMapping public List<Bus> all(){ return repo.findAll(); }

    @PostMapping public ResponseEntity<?> create(@RequestBody Bus b){ return ResponseEntity.ok(repo.save(b)); }

    @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id); return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus updatedBus){
        return repo.findById(id)
            .map(bus -> {
                bus.setBusNumber(updatedBus.getBusNumber());
                bus.setOperatorName(updatedBus.getOperatorName());
                bus.setTotalSeats(updatedBus.getTotalSeats());
                bus.setBusType(updatedBus.getBusType());
                repo.save(bus);
                return ResponseEntity.ok(bus);
            })
            .orElse(ResponseEntity.notFound().build());
    }

}
