package com.test.musala.controller;

import com.test.musala.model.Gateway;
import com.test.musala.repository.GatewayRepository;
import com.test.musala.repository.PeripheralRepository;
import com.test.musala.support.SupportTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gateways")
public class GatewaysController {

    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private PeripheralRepository peripheralRepository;


    @GetMapping
    public List<Gateway> getAllGateways() {
        return (List<Gateway>) gatewayRepository.findAll();
    }

    private boolean checkIpAddressExistence(List<Gateway> gateways, String ipAddress) {
        return gateways.stream().anyMatch(x -> x.getIpAddress().equalsIgnoreCase(ipAddress));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gateway> getGatewayById(@PathVariable(value = "id") String id) {
        return gatewayRepository.findById(id).map(
                gateway -> ResponseEntity.ok().body(gateway))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addGateway(@Valid @RequestBody Gateway gateway) throws NoSuchAlgorithmException {
        if (!SupportTools.validateIP4Address(gateway.getIpAddress())) {
            return ResponseEntity.ok().body("Ip address format is not correct!");
        }
        boolean isRepeated = checkIpAddressExistence((List<Gateway>) gatewayRepository.findAll(), gateway.getIpAddress());
        if (isRepeated)
            return ResponseEntity.ok().body("Ip address already exist in the gateway!");

        String serial = SupportTools.toHash(gateway.getName() + gateway.getIpAddress());
        boolean itIs = gatewayRepository.findById(serial).isPresent();
        if (!itIs) {
            gateway.setSerialNumber(serial);
            gatewayRepository.save(gateway);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(gateway.getSerialNumber()).toUri();
            return ResponseEntity.created(location).body(gateway);
        }
        return ResponseEntity.ok().body("This Gateway already exist!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gateway> updateGateway(@PathVariable(value = "id") String id, @Valid @RequestBody Gateway gateway) {
        if (SupportTools.validateIP4Address(gateway.getIpAddress())) {
            Optional<Gateway> optionalGateway = gatewayRepository.findById(id);
            if (!optionalGateway.isPresent())
                return ResponseEntity.unprocessableEntity().build();

            optionalGateway.get().setName(gateway.getName());
            optionalGateway.get().setIpAddress(gateway.getIpAddress());
            gatewayRepository.save(optionalGateway.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeGateway(@PathVariable(value = "id") String id) {
        if (id.equals("*")) {
            gatewayRepository.deleteAll();
            return ResponseEntity.ok().body("Gateway deleted successfully!");
        }
        Gateway gateway = gatewayRepository.findById(id).orElse(null);
        if (gateway != null) {
            gatewayRepository.delete(gateway);
            return ResponseEntity.ok().body("Gateway deleted successfully!");
        }
        return ResponseEntity.notFound().build();
    }
}
