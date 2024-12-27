package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Forms.ProductForm;
import com.sml.smartledger.Forms.ServiceForm;
import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.inventory.UnitType;
import com.sml.smartledger.Services.interfaces.ImageService;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.sml.smartledger.Helper.AppConstants.unitList;
import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users/inventory/services")
public class ServiceController {

    ServicesService serviceService;
    UserService userService;
    private ImageService imageService;

    @Autowired
    public ServiceController(ServicesService serviceService, UserService userService, ImageService imageService) {
        this.userService = userService;
        this.serviceService = serviceService;
        this.imageService = imageService;
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceForm serviceForm, Authentication authentication) throws IOException, ParseException {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Service service = new Service();
        service.setName(serviceForm.getName());
        service.setServicePrice(serviceForm.getServicePrice());
        service.setUnitType(UnitType.valueOf(serviceForm.getUnitType()));
        String fileName = UUID.randomUUID().toString();
        String serviceImageLink = "";
        if (!serviceForm.getServiceImage().isEmpty()) {
            serviceImageLink = imageService.uploadImage(serviceForm.getServiceImage(), fileName);
        }
        service.setCloudinaryImagePublicId(fileName);
        service.setImage((serviceImageLink == null || serviceImageLink.isEmpty()) ? AppConstants.DEFAULT_PRODUCT_IMAGE_LINK : serviceImageLink);
        service.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(serviceForm.getDate()));
        service.setBusiness(business);
        serviceService.addService(service);
        return "redirect:/users/inventory/services/view";
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
    public String Services(Model model, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Service> serviceList = serviceService.getAllServiceByBusinessId(business.getId());

        ServiceForm serviceForm = new ServiceForm();
        serviceForm.setUnitType(UnitType.PCS.name());
        serviceForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        model.addAttribute("items", serviceList);
        model.addAttribute("itemType", "Service");
        model.addAttribute("unitList", unitList);
        model.addAttribute("selectedBusiness", business);
        model.addAttribute("serviceForm", serviceForm);

        return "/user/item/services/services";
    }
}
