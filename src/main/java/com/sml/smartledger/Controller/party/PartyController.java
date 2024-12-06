package com.sml.smartledger.Controller.party;


import com.sml.smartledger.Forms.PartyForm;
import com.sml.smartledger.Forms.PartyTransactionForm;
import com.sml.smartledger.Helper.Helper;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Model.party.PartyType;
import com.sml.smartledger.Model.party.TransactionType;
import com.sml.smartledger.Repository.party.PartyRepository;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.business.BusinessService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users/party")
public class PartyController {
    final
    PartyService partyService;
    PartyTransactionService partyTransactionService;
    private UserService userService;

    BusinessService businessService;

    Logger logger = LoggerFactory.getLogger(PartyController.class);

    @Autowired
    public PartyController(PartyService partyService, PartyTransactionService partyTransactionService, UserService userService, BusinessService businessService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.userService = userService;
        this.businessService = businessService;
    }

    @PostMapping("/creteParty")
    public String createParty( @ModelAttribute PartyForm partyForm, Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();
        Party party = Party.builder()
                .name(partyForm.getName())
                .mobile(partyForm.getNumber())
                .partyType(PartyType.valueOf(partyForm.getPartyType()))
                .houseNumber(partyForm.getHouseNumber())
                .area(partyForm.getArea())
                .city(partyForm.getCity())
                .state(partyForm.getState())
                .pincode(partyForm.getPincode())
                .gstIN(partyForm.getGstIN())
                .business(business)
                .balance(0d)
                .build();

        party = partyService.createParty(party);
        Double balance = partyForm.getBalance();
        if (balance != null && balance != 0) {
            PartyTransaction transaction = PartyTransaction.builder()
                    .amount(partyForm.getBalance())
                    .party(party)
                    .transactionType(partyForm.getTransectionType().equals("you-gave") ? TransactionType.DEBIT : TransactionType.CREDIT)
                    .description("Opening Balance")
                    .build();
            PartyTransaction partyTransaction = partyTransactionService.createTransaction(transaction);


        }


        return "redirect:/users/party/customer";
    }

    @GetMapping("/{businessId}")
    public ResponseEntity< List<Party>> getAllParty(@PathVariable("businessId") Long businessId){
        List<Party> createdPartyList = partyService.getAllParty(businessId);
        return new ResponseEntity<>(createdPartyList, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Party> deleteParty(@PathVariable Long id){
          partyService.deleteParty(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/customer")
    public String customer(Model model,Authentication authentication){

        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        List<Party> partyList = partyService.getCustomerParty(user.getSelectedBusiness().getId());

        PartyForm partyForm = new PartyForm();
        partyForm.setPartyType("CUSTOMER");
        model.addAttribute("partyForm", partyForm);
        model.addAttribute("partyType", "customer");
        model.addAttribute("parties", partyList);
        return "user/party/customers";
    }

    @GetMapping("/supplier")
    public String supplier(Model model,Authentication authentication){

        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        PartyForm partyForm = new PartyForm();
        partyForm.setBalance(0d);
        partyForm.setPartyType("SUPPLIER");
//        List<Party> partyList = partyService.getSupplierParty(user.getSelectedBusiness().getId());
        model.addAttribute("partyForm", partyForm);
        model.addAttribute("partyType", "supplier");
        model.addAttribute("parties", "supplier");
        return "user/party/suppliers";
    }

    @GetMapping("/bulk_upload")
    public String bulkUpload(Model model,Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        model.addAttribute("partyForm", new PartyForm());
        model.addAttribute("file", null);
//        model.addAttribute("partyType", "bulk_upload");
//        model.addAttribute("parties", "bulk_upload");
        return "user/party/bulk_upload";
    }
    @GetMapping("/download-template")
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        String filePath = "src/main/resources/static/template.xlsx"; // Path to the uploaded file
        try {
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=template.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-data")
    public ResponseEntity<Map<String, Object>> uploadData(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        logger.info("File uploaded: " + file.getOriginalFilename());

        Map<String, Object> response = new HashMap<>();
        List<String> failureReasons = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        if (file.isEmpty()) {
            response.put("status", "failed");
            response.put("message", "Please select a file to upload");
            return ResponseEntity.badRequest().body(response);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            response.put("status", "failed");
            response.put("message", "Please upload an Excel file (xlsx)");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Check file type
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                response.put("status", "failed");
                response.put("message", "Invalid file format. Please upload an Excel file.");
                return ResponseEntity.badRequest().body(response);
            }

            // Process the file
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            logger.info("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Sheet name: " + sheet.getSheetName());

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                try {
                    // Skip empty rows
                    if (row.getCell(0).getStringCellValue().isEmpty()) break;

                    // Create a new Party
                    Party party = new Party();
                    party.setPartyType(PartyType.valueOf(row.getCell(0).getStringCellValue()));
                    logger.info("Party Type: " + party.getPartyType());
                    party.setName(row.getCell(1).getStringCellValue());
                    logger.info("Party Name: " + party.getName());
                    party.setMobile(String.valueOf((long) row.getCell(2).getNumericCellValue()));

                    String var = row.getCell(4).getStringCellValue().equals("YOU_GAVE") ? "You_Gave" : "You_Got";
                    party.setGstIN(row.getCell(5).getStringCellValue());
                    party.setBalance(0d);
                    party.setHouseNumber(row.getCell(6).getStringCellValue());
                    party.setArea(row.getCell(7).getStringCellValue());
                    party.setCity(row.getCell(9).getStringCellValue());
                    party.setState(row.getCell(10).getStringCellValue());
                    party.setPincode(String.valueOf((int) row.getCell(8).getNumericCellValue()));
                    party.setBusiness(user.getSelectedBusiness());

                    Party savedParty = partyService.createParty(party);

                    // Process opening balance
                    double balance = row.getCell(3).getNumericCellValue(); // Opening balance
                    if (balance != 0) {
                        PartyTransaction transaction = PartyTransaction.builder()
                                .amount(balance)
                                .party(savedParty)
                                .transactionType(var.equals("You_Gave") ? TransactionType.DEBIT : TransactionType.CREDIT)
                                .description("Opening Balance")
                                .build();
                        partyTransactionService.createTransaction(transaction);
                    }

                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    failureReasons.add("Row " + (row.getRowNum() + 1) + ": " + ex.getMessage());
                }
            }

            workbook.close();

            response.put("status", "success");
            response.put("successfulContacts", successCount);
            response.put("failedContacts", failCount);
            response.put("failureReasons", failureReasons);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing file: " + e.getMessage());
            response.put("status", "failed");
            response.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view/{id}")
    public String viewParty(@PathVariable("id") Long partyId, Model model,Authentication authentication){
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        Party party = partyService.getPartyById(partyId);
        PartyTransactionForm partyTransactionForm = new PartyTransactionForm();
        partyTransactionForm.setPartyId(partyId);
        String date1 = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        partyTransactionForm.setDate(date1);
        model.addAttribute("party", party);
        model.addAttribute("PartyTransactionForm",partyTransactionForm);
        return "user/party/party_details";
    }

    @PostMapping("/create-party-transaction")
    public String createPartyTransaction( @ModelAttribute PartyTransactionForm partyTransactionForm){
         logger.info("Party Transaction Form: " + partyTransactionForm.getPartyId());
        Party party = partyService.getPartyById(partyTransactionForm.getPartyId());
        Date date = Helper.convertStringToDate(partyTransactionForm.getDate());
        assert date != null;
        date.setTime(new Date().getTime());
        PartyTransaction transaction = PartyTransaction.builder()
                .amount(partyTransactionForm.getAmount())
                .party(party)
                .transactionDate(date)
                .transactionType(TransactionType.valueOf(partyTransactionForm.getTransactionType()))
                .description(partyTransactionForm.getDescription())
                .build();

        partyTransactionService.createTransaction(transaction);


        return "redirect:/users/party/view/"+partyTransactionForm.getPartyId();
    }

}
