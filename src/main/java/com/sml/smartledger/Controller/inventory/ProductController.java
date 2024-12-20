package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
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
@RequestMapping("users/inventory/products")
public class ProductController {
    ProductService productService;
    UserService userService;
    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product billProduct = productService.addProduct(product);
        return new ResponseEntity<>(billProduct, HttpStatus.CREATED);

    }
    @GetMapping("/{billId}")
    public ResponseEntity<List<Product>> getAllProductByBusinessId(@PathVariable("billId") Long businessId){
        List<Product> billProductList = productService.getAllProductByBusinessId(businessId);
        return new ResponseEntity<>(billProductList,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public String Products(@ModelAttribute Model model, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Product> ProductList = productService.getAllProductByBusinessId(business.getId());

        model.addAttribute("item", ProductList);
        model.addAttribute("itemType", "Product");
        model.addAttribute("selectedBusiness", business);

        return "/user/item/products";
    }

}
