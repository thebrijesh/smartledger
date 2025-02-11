package com.sml.smartledger.Services.interfaces.bill;

import com.sml.smartledger.Model.bill.Bill;

import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> getAllBills(Long businessId);
    List<Bill> getPurchaseBills(Long businessId);
    List<Bill> getAllSaleBills(Long businessId);
    Bill createBill(Bill bill);
    Bill updateBill(Bill bill);
    Bill getBillById(Long id);
    void deleteBill(Long id);
}
