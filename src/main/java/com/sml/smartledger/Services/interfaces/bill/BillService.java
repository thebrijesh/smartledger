package com.sml.smartledger.Services.interfaces.bill;

import com.sml.smartledger.Model.bill.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getAllBills(Long businessId);
    List<Bill> getAllSaleBills(Long businessId);
    Bill createBill(Bill bill);
    Bill updateBill(Bill bill);

}
