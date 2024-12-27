package com.sml.smartledger.Controller.inventory;


import com.sml.smartledger.Forms.ProductForm;
import com.sml.smartledger.Forms.ProductTransactionForm;
import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.inventory.ProductTransaction;
import com.sml.smartledger.Model.inventory.StockTransactionType;
import com.sml.smartledger.Model.inventory.UnitType;
import com.sml.smartledger.Services.interfaces.ImageService;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.inventory.ProductService;
import com.sml.smartledger.Services.interfaces.inventory.ProductTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.sml.smartledger.Helper.AppConstants.unitList;
import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/inventory/products")
public class ProductController {
    ProductService productService;
    ProductTransactionService productTransactionService;
    UserService userService;
    ImageService imageService;
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    BusinessService businessService;

    @Autowired
    public ProductController(BusinessService businessService, ProductService productService, UserService userService, ProductTransactionService productTransactionService, ImageService imageService) {
        this.productService = productService;
        this.userService = userService;
        this.productTransactionService = productTransactionService;
        this.businessService = businessService;
        this.imageService = imageService;
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductForm productForm, Authentication authentication) throws ParseException, IOException {
        logger.info("productForm: {}", productForm);
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Product product = new Product();
        product.setName(productForm.getName());
        product.setSalePrice(productForm.getSalePrice());
        product.setLowStock(productForm.getLowStock());
        logger.info("UnitType: {}", productForm.getUnitType());
        product.setUnitType(UnitType.valueOf(productForm.getUnitType()));
        String fileName = UUID.randomUUID().toString();
        String productImageLink = "";
        if (!productForm.getProductImage().isEmpty()) {
            productImageLink = imageService.uploadImage(productForm.getProductImage(), fileName);
        }
        product.setCloudinaryImagePublicId(fileName);
        product.setImage((productImageLink == null || productImageLink.isEmpty()) ? AppConstants.DEFAULT_PRODUCT_IMAGE_LINK : productImageLink);
//        product.setUnitType(UnitType.valueOf(productForm.getUnitType()));
        product.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(productForm.getDate()));

        product.setBusiness(business);
        productService.addProduct(product);
        //if the opening stock is greater than 0 then add a transaction
        if (productForm.getOpeningStock() > 0) {
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setAmount(productForm.getPurchasePrice());
            productTransaction.setUnit(productForm.getOpeningStock());
            productTransaction.setStockTransactionType(StockTransactionType.IN);
            productTransaction.setProduct(product);
            productTransaction.setDate(Helper.combineDate(productForm.getDate(), new Date()));
            productTransaction.setDescription("Opening Stock");
            productTransactionService.addProductTransaction(productTransaction);

        }

        if (isProductLowStock(product)) {
            business.setLowStockProducts(business.getLowStockProducts() + 1);
        }
        businessService.saveBusiness(business);
        return "redirect:/users/inventory/products/view";

    }

    @GetMapping("/{billId}")
    public ResponseEntity<List<Product>> getAllProductByBusinessId(@PathVariable("billId") Long businessId) {
        List<Product> billProductList = productService.getAllProductByBusinessId(businessId);
        return new ResponseEntity<>(billProductList, HttpStatus.OK);
    }



    @GetMapping("/view")
    public String Products(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Product> ProductList = productService.getAllProductByBusinessId(business.getId());

        ProductForm productForm = new ProductForm();
        productForm.setUnitType(UnitType.PCS.name());
        productForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        model.addAttribute("items", ProductList);
        model.addAttribute("itemType", "Product");
        model.addAttribute("unitList", unitList);
        model.addAttribute("productForm", productForm);
        model.addAttribute("selectedBusiness", business);

        return "/user/item/products";
    }

    public boolean isProductLowStock(Product product) {
        return product.getStockQuantity() < product.getLowStock();
    }

    @GetMapping("/details/{id}")
    public String productDetails(@PathVariable("id") Long id, Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        Product product = productService.getProductById(id);
        ProductTransactionForm productTransactionForm = new ProductTransactionForm();
        productTransactionForm.setProductId(product.getId());
        productTransactionForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        ProductForm productForm = new ProductForm();
        productForm.setDate(new SimpleDateFormat("dd-MM-yyyy").format(product.getDate()));
        productForm.setUnitType(product.getUnitType().name());
        productForm.setLowStock(product.getLowStock());
        productForm.setName(product.getName());
        productForm.setSalePrice(product.getSalePrice());
        productForm.setPurchasePrice(product.getPurchasePrice());
        productForm.setPicture(product.getImage());
        productForm.setOpeningStock(product.getStockQuantity());
        productForm.setId(product.getId());
        model.addAttribute("itemType", "Product");

        model.addAttribute("unitList", unitList);

        model.addAttribute("product", product);
        model.addAttribute("ProductTransactionForm", productTransactionForm);
        model.addAttribute("productForm", productForm);

        model.addAttribute("selectedBusiness", business);
        return "user/item/product_details";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductForm productForm) throws ParseException, IOException {
        Product product = new Product();
        System.out.println("guhhb"+productForm.getId());
        System.out.println("guhhb"+productForm.getName());
        product.setId(productForm.getId());
        product.setName(productForm.getName());
        product.setSalePrice(productForm.getSalePrice());
        product.setLowStock(productForm.getLowStock());
        product.setUnitType(UnitType.valueOf(productForm.getUnitType()));
        product.setPurchasePrice(productForm.getPurchasePrice());
        product.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(productForm.getDate()));
        String fileName = UUID.randomUUID().toString();
        if (!productForm.getProductImage().isEmpty()) {
            String productImageLink = imageService.uploadImage(productForm.getProductImage(), fileName);
            product.setCloudinaryImagePublicId(fileName);
            product.setImage(productImageLink);
        }

        productService.updateProduct(product);

        return "redirect:/users/inventory/products/view";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
