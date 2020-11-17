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
import java.util.Optional;

@RestController
@RequestMapping("/peripherals")
public class PeripheralsController {

    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private PeripheralRepository peripheralRepository;

    @GetMapping
    public List<Peripheral> getAllPeripherals() {
        return (List<Peripheral>) peripheralRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Peripheral> getPeripheralById(@PathVariable(value = "id") Long id) {
        Optional<Peripheral> optionalPeripheral = peripheralRepository.findById(id);
        return optionalPeripheral.map(peripheral -> ResponseEntity.ok().body(peripheral)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{gId}")
    public ResponseEntity<?> addPeripheral(@PathVariable(value = "gId") String gId, @Valid @RequestBody Peripheral p) {
        if (p == null)
            return ResponseEntity.notFound().build();

        if (!SupportTools.dateValidator.test(p.getCreated().toString()))
            return ResponseEntity.ok().body("Bad format for the creation date!");

        Gateway gateway = gatewayRepository.findById(gId).orElse(null);
        if (gateway != null && gateway.getPeripheralIdlList().size() <= 10) {
            p.setGateway(gateway);
            return ResponseEntity.ok().body(peripheralRepository.save(p));
        }
        return ResponseEntity.ok().body("Issues while creating the peripheral device!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePeripheral(@PathVariable(value = "id") Long id, @Valid @RequestBody Peripheral p) {
        Optional<Gateway> optionalGateway = gatewayRepository.findById(p.getGateway().getSerialNumber());
        if (!optionalGateway.isPresent())
            return ResponseEntity.unprocessableEntity().build();

        Optional<Peripheral> optionalPeripheral = peripheralRepository.findById(id);
        if (!optionalPeripheral.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            Peripheral peripheral = optionalPeripheral.get();
            peripheral.setGateway(optionalGateway.get());
            peripheral.setStatus(p.getStatus());
            peripheral.setVendor(p.getVendor());
            peripheral.setCreated(p.getCreated());
            return ResponseEntity.ok().body(peripheralRepository.save(peripheral));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removePeripherals(@PathVariable(value = "gId") Long id) {
        Optional<Peripheral> optionalPeripheral = peripheralRepository.findById(id);
        if (optionalPeripheral.isPresent()) {
            Peripheral p = optionalPeripheral.get();
            peripheralRepository.delete(p);
            return ResponseEntity.ok().body("Peripheral deleted successfully!");
        }
        return ResponseEntity.notFound().build();
    }
}
