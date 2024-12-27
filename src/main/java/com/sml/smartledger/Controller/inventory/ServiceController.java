package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Forms.ProductForm;
import com.sml.smartledger.Forms.ProductTransactionForm;
import com.sml.smartledger.Forms.ServiceForm;
import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.inventory.UnitType;
import com.sml.smartledger.Services.interfaces.ImageService;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.inventory.ServicesService;
import org.slf4j.Logger;
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

    ServicesService servicesService;
    UserService userService;
    private ImageService imageService;
    Logger logger = org.slf4j.LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    public ServiceController(ServicesService servicesService, UserService userService, ImageService imageService) {
        this.userService = userService;
        this.servicesService = servicesService;
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
        servicesService.addService(service);
        return "redirect:/users/inventory/services/view";
    }
    @GetMapping("/{billServiceId}")
    public ResponseEntity<List<Service>> getAllBillServicesById(@PathVariable("billServiceId") Long billServiceId){
        List<Service> billServices = servicesService.getAllServiceByBusinessId(billServiceId);
        return new ResponseEntity<>(billServices,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServices(@PathVariable("id") Long id){
        servicesService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/view")
    public String Services(Model model, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Service> serviceList = servicesService.getAllServiceByBusinessId(business.getId());

        ServiceForm serviceForm = new ServiceForm();
        serviceForm.setUnitType(UnitType.PCS.name());
        serviceForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        //log for print serviceForm
        serviceList.forEach(service -> {logger.info("service: {}", service);});
        logger.info("serviceForm: {}", serviceForm);
        logger.info("business: {}", business);
        logger.info("unitList: {}", unitList);
        model.addAttribute("items", serviceList);
        model.addAttribute("itemType", "Service");
        model.addAttribute("unitList", unitList);
        model.addAttribute("selectedBusiness", business);
        model.addAttribute("serviceForm", serviceForm);

        return "/user/item/services/services";
    }

    @GetMapping("/details/{id}")
    public String serviceDetails(@PathVariable("id") Long id, Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Service service = servicesService.getServiceById(id);
        ServiceForm serviceForm = new ServiceForm();
        serviceForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(service.getDate()));
        serviceForm.setUnitType(service.getUnitType().name());
        serviceForm.setName(service.getName());
        serviceForm.setServicePrice(service.getServicePrice());
        serviceForm.setPicture(service.getImage());
        serviceForm.setId(service.getId());
        model.addAttribute("itemType", "Service");

        model.addAttribute("unitList", unitList);

        model.addAttribute("service", service);
        model.addAttribute("serviceForm", serviceForm);

        model.addAttribute("selectedBusiness", business);
        return "user/item/services/service_details";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") Long id) {
        servicesService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public String updateService(@ModelAttribute ServiceForm serviceForm) throws ParseException, IOException {
        logger.info("serviceForm: {}", serviceForm);
        Service service = new Service();
        service.setId(serviceForm.getId());
        service.setName(serviceForm.getName());
        service.setServicePrice(serviceForm.getServicePrice());
        service.setUnitType(UnitType.valueOf(serviceForm.getUnitType()));
        service.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(serviceForm.getDate()));
        String fileName = UUID.randomUUID().toString();
        if (!serviceForm.getServiceImage().isEmpty()) {
            String serviceImageLink = imageService.uploadImage(serviceForm.getServiceImage(), fileName);
            service.setCloudinaryImagePublicId(fileName);
            service.setImage(serviceImageLink);
        }
        servicesService.updateService(service);
        return "redirect:/users/inventory/services/view";
    }
}
