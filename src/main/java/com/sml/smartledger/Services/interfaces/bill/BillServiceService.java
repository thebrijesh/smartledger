package com.sml.smartledger.Services.interfaces.bill;




import com.sml.smartledger.Model.bill.BillService;

import java.util.List;

public interface BillServiceService {
    public BillService addService(BillService service);
    public List<BillService> getAllServiceByBusinessId(Long serviceId);
    public void deleteService(Long id);
}
