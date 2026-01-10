package com.example.BillingService.controller;

import java.util.List;
import java.util.Optional;

import com.example.BillingService.entity.BillDetailsDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BillingService.entity.Bill;
import com.example.BillingService.entity.BillDTO;
import com.example.BillingService.service.BillService;

@RestController
public class BillingController {
    private final BillService billService;

    public BillingController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/bills")
    @ResponseBody
    public Bill createBill(@RequestBody BillDetailsDTO bill) {
        return billService.creatBill(bill);
    }

    @GetMapping("/bills")
    @ResponseBody
    public List<Bill> getBills() {
        return billService.listBills();
    }

    @GetMapping("/bills/{id}")
    @ResponseBody
    public Optional<Bill> getBills(@PathVariable int id) {
        return billService.getBill(id);
    }

    @PutMapping("/bills/{id}")
    @ResponseBody
    public Bill updateBills(@RequestBody BillDetailsDTO bill, @PathVariable int id) throws Exception{
        return billService.updateBill(id, bill);
    }

    @DeleteMapping("bills/{id}")
    public void deleteBill(@PathVariable int id) {
        billService.deleteBill(id);
    }

    @GetMapping("/bills/patient/{id}")
    @ResponseBody
    public List<Bill> searchBillsByPatientId(@PathVariable int id) {
        return billService.searchBillByPatientId(id);
    }
}
