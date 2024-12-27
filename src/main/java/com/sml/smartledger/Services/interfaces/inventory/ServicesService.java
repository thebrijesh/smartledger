package com.sml.smartledger.Services.interfaces.inventory;


import com.sml.smartledger.Model.inventory.Service;

import java.util.List;

public interface ServicesService {
    public Service addService(Service service);
    public List<Service> getAllServiceByBusinessId(Long serviceId);
    public void deleteService(Long id);

    Service getServiceById(Long id);

    void updateService(Service service);
}
