# Useful information for this rest service
##Data base configuration:
```
/*As described and can be modify in application.properties*/
Database
name = gateways
user = gateway
password = lfJFYDmRUrrzZZF8
/*These tables are generated by Hibernate. No need for you to create them manually!*/
table_gateway=gateway
table_peripheral=peripheral_device
```

##Functionalities of the service
<h5>Over Gateways</h5>
<h5>@RequestMapping("/gateways")</h5>
```
http://localhost:8080/gateways

/*Get all gateways*/
Name: getAllGateways()
Access sample on terminal: curl http://localhost:8080/gateways

/*Get a gateway*/
Name: getGatewayById(id)
Mapping: /{id}
Access sample on terminal: curl http://localhost:8080/gateways/1

/*Add gateway*/
Name: addGateway(gateway)
Note: Gateway id is a 16 character hash generated by the combination of name and ipAddress(this value must not be passed)
Note: Peripheral list for the gateway will be created empty. You can later add 10 peripheral devices top.
Access sample on terminal: curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Gateway1\",\"ipAddress\":\"192.168.0.100\"}" http://localhost:8080/gateways

/*Update gateway*/
Name: updateGateway(id, gateway)
Mapping: /{id}
Access sample on terminal: curl -X PUT -H "Content-Type: application/json" -d "{\"name\":\"Gateway1\",\"ipAddress\":\"192.168.0.100\"}" http://localhost:8080/gateways/1

/*Remove gateway*/
Name: removeGateway(id)
Mapping: /{id}
Warning: if you pass "*" as the value of the id all the gateways will be deleted. Example curl -X DELETE http://localhost:8080/gateways/remove/*
Access sample on terminal: curl -X DELETE http://localhost:8080/gateways/1
```


<h5>Over Peripheral devices</h5>
<h5>@RequestMapping("/peripherals")</h5>
```
http://localhost:8080/peripherals

/*Get all peripheral devices*/
Name: getAllPeripherals()
Access sample on terminal: curl http://localhost:8080/peripherals/

/*Get a peripheral device*/
Name: getPeripheralById(id)
Mapping: /{id}
Access sample on terminal: curl http://localhost:8080/peripherals/1

/*Add peripheral device*/
Name: addPeripheral(p)
Mapping: /{gId}
Note: UID is generated. Date must be in the current format (yyyy-mm-dd)
Note: gId is the ID of the current Gateway to add this device
Access sample on terminal: curl -X POST -H "Content-Type: application/json" -d "{\"Vendor\":\"Vendor name\",\"Created at\":\"2020-11-14\", \"state\":\"online\"}" http://localhost:8080/peripherals/f44f1a16d93c110cf49193bdf7c4e8c6

/*Update peripheral device*/
Name: updatePeripheral(id, Peripheral)
Mapping: /{id}
Access sample on terminal: curl -X PUT -H "Content-Type: application/json" -d "{\"Vendor\":\"Vendor name\",\"Created at\":\"2020-11-14\", \"state\":\"online\"}" http://localhost:8080/peripherals/1

/*Remove peripheral device*/
Name: removePeripherals(id)
Mapping: /{id}
Access sample on terminal: curl -X DELETE http://localhost:8080/peripherals/remove/1
```