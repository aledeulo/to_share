package com.test.musala.controller;

import com.test.musala.model.Gateway;
import com.test.musala.model.Peripheral;
import com.test.musala.repository.GatewayRepository;
import com.test.musala.repository.PeripheralRepository;
import com.test.musala.support.SupportTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/peripherals")
public class PeripheralsController {

    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private PeripheralRepository peripheralRepository;

    @GetMapping("/get")
    public List<Peripheral> getAllPeripherals() {
        return (List<Peripheral>) peripheralRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPeripheral(@Valid @RequestBody Peripheral p) {
        if (p == null)
            return ResponseEntity.notFound().build();

        if (!SupportTools.dateValidator.test(p.getCreated().toString()))
            return ResponseEntity.ok().body("Bad format for the creation date!");

        peripheralRepository.save(p);
        return ResponseEntity.ok().body(p);
    }

    @GetMapping("/getPeripheral/{id}")
    public ResponseEntity<Peripheral> getPeripheralById(@PathVariable(value = "id") String id) {
        if (SupportTools.isANumber(id)) {
            return peripheralRepository.findById(Long.parseLong(id))
                    .map(peripheral -> ResponseEntity.ok().body(peripheral))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/updatePeripheral/{id}")
    public ResponseEntity<Peripheral> updatePeripheral(@PathVariable(value = "id") String id, @Valid @RequestBody Peripheral p) {
        if (SupportTools.isANumber(id)) {
            return peripheralRepository.findById(Long.parseLong(id))
                    .map(peripheral -> {
                        peripheral.setStatus(p.getStatus());
                        peripheral.setVendor(p.getVendor());
                        peripheral.setCreated(p.getCreated());
                        return ResponseEntity.ok()
                                .body(peripheralRepository.save(peripheral));
                    }).orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delPeripheral/{id}")
    public ResponseEntity<?> removePeripherals(@PathVariable(value = "id") String id) {
        if (SupportTools.isANumber(id)) {
            Peripheral p = peripheralRepository.findById(Long.parseLong(id))
                    .orElse(null);
            if (p != null) {
                List<Gateway> gatewayList = (List<Gateway>) gatewayRepository.findAll();
                gatewayList.forEach(g -> {
                    boolean exist = g.getPeripheralIdlList().stream().anyMatch(x -> x == Long.parseLong(id));
                    if (exist) {
                        SupportTools.removeStringFromArray(g.getPeripheralIdlList(), Long.parseLong(id));
                        gatewayRepository.save(g);
                    }
                });
                peripheralRepository.delete(p);
                return ResponseEntity.ok().body("Peripheral deleted successfully!");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
