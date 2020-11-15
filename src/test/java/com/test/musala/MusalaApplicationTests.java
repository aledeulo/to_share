package com.test.musala;


import com.test.musala.model.Gateway;
import com.test.musala.model.Peripheral;
import com.test.musala.support.SupportTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MusalaApplicationTests extends MusalaAbstractTest {

    private MockMvc mvc;

    /*TESTS OVER GATEWAYS*/
    @Test
    public void getAllGateways() throws Exception {
        String path = "/gateways/get";
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Gateway[] gatewayList = super.mapFromJson(content, Gateway[].class);
        assertTrue(gatewayList.length > 0);
    }

    @Test
    public void addGateway() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String path = "/gateways/add";
        String name = "Gateway172";
        String ipAddress = "172.20.10.11";
        String hash = SupportTools.toHash(name + ipAddress);
        Gateway gateway = new Gateway(hash, name, ipAddress, new ArrayList<>());
        String inputJson = super.mapToJson(gateway);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "This gateway was created successfully: " + gateway);
    }

    @Test
    public void wrongIpAddress() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String path = "/gateways/add";
        String name = "Gateway172";
        String ipAddress = "172.20.10.112222";
        String hash = SupportTools.toHash(name + ipAddress);
        Gateway gateway = new Gateway(hash, name, ipAddress, new ArrayList<>());
        String inputJson = super.mapToJson(gateway);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Ip address format is not correct!");
    }

    /*TEST OVER PERIPHERAL DEVICES*/
    @Test
    public void getAllPeripheralDevices() throws Exception {
        String path = "/peripherals/get";
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Peripheral[] peripherals = super.mapFromJson(content, Peripheral[].class);
        assertTrue(peripherals.length > 0);
    }

    @Test
    public void addPeripheralDevice() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String path = "/peripherals/add";
        String vendor = "Vendor172";
        String vStatus = "online";
        Date created = new Date(new java.util.Date().getTime());
        Peripheral peripheral = new Peripheral(vendor, vStatus, created);
        String inputJson = super.mapToJson(peripheral);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        boolean answer = content.contains("Peripheral device created successfully: ");
        Assertions.assertTrue(answer);
    }

}
