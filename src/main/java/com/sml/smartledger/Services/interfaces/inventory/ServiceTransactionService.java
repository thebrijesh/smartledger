package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.ServiceTransaction;

import java.util.List;

public interface ServiceTransactionService {
    List<ServiceTransaction> getAllServiceTransaction(Long billServiceId);
    ServiceTransaction addServiceTransaction(ServiceTransaction serviceTransaction);
}
