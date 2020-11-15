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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gateways")
public class GatewaysController {

    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private PeripheralRepository peripheralRepository;

    @GetMapping("/get")
    public List<Gateway> getAllGateways() {
        return (List<Gateway>) gatewayRepository.findAll();
    }

    private boolean checkIpAddressExistence(List<Gateway> gateways, String ipAddress) {
        return gateways.stream().anyMatch(x -> x.getIpAddress().equalsIgnoreCase(ipAddress));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addGateway(@Valid @RequestBody Gateway gateway) throws NoSuchAlgorithmException {
        if (SupportTools.validateIP4Address(gateway.getIpAddress())) {
            boolean isRepeated = checkIpAddressExistence((List<Gateway>) gatewayRepository.findAll(), gateway.getIpAddress());
            if (isRepeated)
                return ResponseEntity.ok().body("Ip address already exist in the gateway!");

            String serial = SupportTools.toHash(gateway.getName() + gateway.getIpAddress());
            boolean itIs = gatewayRepository.findById(serial).isPresent();
            if (!itIs) {
                gateway.setSerialNumber(serial);
                gateway.setPeripheralIdlList(new ArrayList<>());
                gatewayRepository.save(gateway);
                return ResponseEntity.ok().body("This gateway was created successfully: " + gateway);
            }
            return ResponseEntity.ok().body("This Gateway already exist!");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Gateway> getGatewayById(@PathVariable(value = "id") String id) {
        return gatewayRepository.findById(id).map(
                gateway -> ResponseEntity.ok().body(gateway))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Gateway> updateGateway(@PathVariable(value = "id") String id, @Valid @RequestBody Gateway gateway) {
        if (SupportTools.validateIP4Address(gateway.getIpAddress())) {
            return gatewayRepository.findById(id).map(
                    record -> {
                        record.setName(gateway.getName());
                        record.setIpAddress(gateway.getIpAddress());
                        if (record.getPeripheralIdlList().size() != gateway.getPeripheralIdlList().size())
                            record.setPeripheralIdlList(gateway.getPeripheralIdlList());
                        Gateway g = gatewayRepository.save(record);
                        return ResponseEntity.ok().body(g);
                    }).orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/remove/{id}")
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

    @PutMapping("/addPeripheral/{gId}/{pId}")
    public ResponseEntity<?> addPeripheralToGateway(@PathVariable(value = "gId") String gId, @PathVariable(value = "pId") String pId) {
        String result = updatePeripheralsInGateway(gId, pId, "add");
        if (result.equals("ok"))
            return ResponseEntity.ok().body("Peripheral device added to the gateway!");
        return ResponseEntity.ok().body("Peripheral could not be added to to the gateway!");
    }

    @PutMapping("/delPeripheral/{gId}/{pId}")
    public ResponseEntity<String> removePeripheralFromGateway(@PathVariable(value = "gId") String gId, @PathVariable(value = "pId") String pId) {
        String result = updatePeripheralsInGateway(gId, pId, "delete");
        if (result.equals("ok"))
            return ResponseEntity.ok().body("Peripheral device deleted from the gateway!");
        return ResponseEntity.notFound().build();
    }

    public String updatePeripheralsInGateway(String gId, String pId, String action) {
        String answer = "wrong";
        if (SupportTools.isANumber(pId) && (gId != null && !gId.equals(""))) {
            Gateway g = gatewayRepository.findById(gId).orElse(null);
            long peripheralId = Long.parseLong(pId);
            Peripheral p = peripheralRepository.findById(peripheralId).orElse(null);
            if (g != null && p != null) {
                boolean exist = g.getPeripheralIdlList().stream().anyMatch(x -> x == peripheralId);
                switch (action) {
                    case "delete":
                        if (exist) {
                            SupportTools.removeStringFromArray(g.getPeripheralIdlList(), peripheralId);
                            gatewayRepository.save(g);
                            answer = "ok";
                        }
                        break;
                    case "add":
                        if (!exist) {
                            if (g.getPeripheralIdlList().size() < 10) {
                                g.getPeripheralIdlList().add(peripheralId);
                                gatewayRepository.save(g);
                                answer = "ok";
                            } else System.out.println("This gateways reached the maximal number of devices!");
                        }
                        break;
                }
                return answer;
            } else System.out.println("Either the gateway_id or peripheral_id is not correct!");
        } else System.out.println("Either the gateway_id or peripheral_id is not correct!");
        return answer;
    }
}
