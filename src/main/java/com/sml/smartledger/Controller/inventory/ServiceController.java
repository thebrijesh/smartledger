package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users/inventory/services")
public class ServiceController {

    ServicesService serviceService;
    UserService userService;
    @Autowired
    public ServiceController(ServicesService serviceService, UserService userService) {
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @PostMapping("/add")
    public ResponseEntity<Service> addService(@RequestBody Service service) {
        Service billService = serviceService.addService(service);
        return new ResponseEntity<>(billService, HttpStatus.CREATED);
    }
    @GetMapping("/{billServiceId}")
    public ResponseEntity<List<Service>> getAllBillServicesById(@PathVariable("billServiceId") Long billServiceId){
        List<Service> billServices = serviceService.getAllServiceByBusinessId(billServiceId);
        return new ResponseEntity<>(billServices,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServices(@PathVariable("id") Long id){
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/view")
    public String Services(@ModelAttribute Model model, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Service> serviceList = serviceService.getAllServiceByBusinessId(business.getId());

        model.addAttribute("item", serviceList);
        model.addAttribute("itemType", "Service");
        model.addAttribute("selectedBusiness", business);

        return "/user/item/services/services";
    }
}
