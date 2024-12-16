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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("/users/party")
public class PartyController {
    private final PartyService partyService;
    private final PartyTransactionService partyTransactionService;
    private final UserService userService;
    private final BusinessService businessService;
    private final Logger logger = LoggerFactory.getLogger(PartyController.class);

    @Autowired
    public PartyController(PartyService partyService, PartyTransactionService partyTransactionService, UserService userService, BusinessService businessService) {
        this.partyService = partyService;
        this.partyTransactionService = partyTransactionService;
        this.userService = userService;
        this.businessService = businessService;
    }

    @PostMapping("/createParty")
    public String createParty(@ModelAttribute PartyForm partyForm, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User saveUser = userService.getUserByEmail(email);
        Business business = saveUser.getSelectedBusiness();
        Party party = buildPartyFromForm(partyForm, business);

        party = partyService.createParty(party);
        createOpeningBalanceTransaction(partyForm, party);

        return party.getPartyType().equals(PartyType.CUSTOMER) ? "redirect:/users/party/customer" : "redirect:/users/party/supplier";
    }

    @PostMapping("/updateParty")
    public String updateParty(@ModelAttribute Party party) {
        Party partyFromDB = partyService.getPartyById(party.getId());
        updatePartyDetails(partyFromDB, party);
        partyService.updateParty(partyFromDB);
        return "redirect:/users/party/view/" + party.getId();
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<Party>> getAllParty(@PathVariable("businessId") Long businessId) {
        List<Party> createdPartyList = partyService.getAllParties(businessId);
        return new ResponseEntity<>(createdPartyList, HttpStatus.OK);
    }

    @GetMapping("/customer")
    public String customer(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        List<Party> partyList = partyService.getCustomerParties(user.getSelectedBusiness().getId());

        PartyForm partyForm = new PartyForm();
        partyForm.setPartyType("CUSTOMER");
        model.addAttribute("partyForm", partyForm);
        model.addAttribute("partyType", "Customer");
        model.addAttribute("parties", partyList);
        return "user/party/customers";
    }

    @GetMapping("/supplier")
    public String supplier(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        PartyForm partyForm = new PartyForm();
        partyForm.setBalance(0d);
        partyForm.setPartyType("SUPPLIER");
        List<Party> partyList = partyService.getSupplierParties(user.getSelectedBusiness().getId());
        model.addAttribute("partyForm", partyForm);
        model.addAttribute("partyType", "Supplier");
        model.addAttribute("parties", partyList);

        return "user/party/suppliers";
    }

    @GetMapping("/bulk_upload")
    public String bulkUpload(Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        model.addAttribute("partyForm", new PartyForm());
        model.addAttribute("file", null);
        return "user/party/bulk_upload";
    }

    @GetMapping("/download-template")
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        String filePath = "src/main/resources/static/template.xlsx";
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

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                try {
                    if (row.getCell(0).getStringCellValue().isEmpty()) break;

                    Party party = buildPartyFromRow(row, user.getSelectedBusiness());
                    Party savedParty = partyService.createParty(party);

                    double balance = row.getCell(3).getNumericCellValue();
                    if (balance != 0) {
                        PartyTransaction transaction = PartyTransaction.builder()
                                .amount(balance)
                                .party(savedParty)
                                .transactionType(row.getCell(4).getStringCellValue().equals("YOU_GAVE") ? TransactionType.DEBIT : TransactionType.CREDIT)
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
    public String viewParty(@PathVariable("id") Long partyId, Model model, Authentication authentication) {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        Party party = partyService.getPartyById(partyId);
        PartyTransactionForm partyTransactionForm = new PartyTransactionForm();
        partyTransactionForm.setPartyId(partyId);
        partyTransactionForm.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("party", party);
        model.addAttribute("PartyTransactionForm", partyTransactionForm);
        return "user/party/party_details";
    }

    @PostMapping("/create-party-transaction")
    public String createPartyTransaction(@ModelAttribute PartyTransactionForm partyTransactionForm) {
        Party party = partyService.getPartyById(partyTransactionForm.getPartyId());
        Date date = Helper.convertStringToDate(partyTransactionForm.getDate());
        assert date != null;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        Date combinedDate = Date.from(localDate.atTime(localTime).atZone(ZoneId.systemDefault()).toInstant());

        PartyTransaction transaction = PartyTransaction.builder()
                .amount(partyTransactionForm.getAmount())
                .party(party)
                .transactionDate(combinedDate)
                .transactionType(TransactionType.valueOf(partyTransactionForm.getTransactionType()))
                .description(partyTransactionForm.getDescription())
                .build();

        partyTransactionService.createTransaction(transaction);
        return "redirect:/users/party/view/" + partyTransactionForm.getPartyId();
    }

    @PostMapping("/update-party-transaction")
    public String updatePartyTransaction(@ModelAttribute PartyTransactionForm partyTransactionForm) {
        logger.info("Party Transaction ID: " + partyTransactionForm.getId());
        PartyTransaction newPartyTransaction = new PartyTransaction();
        newPartyTransaction.setId(partyTransactionForm.getId());
        newPartyTransaction.setAmount(partyTransactionForm.getAmount());
        newPartyTransaction.setDescription(partyTransactionForm.getDescription());

        PartyTransaction partyTransaction = partyTransactionService.getTransactionById(partyTransactionForm.getId());
        logger.info("New Party Transaction date: " + partyTransaction.getCreatedAt());
        Date date = Helper.convertStringToDate(partyTransactionForm.getDate());
        assert date != null;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = partyTransaction.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

// Combine date and time

        Date combinedDate = Date.from(localDate.atTime(localTime).atZone(ZoneId.systemDefault()).toInstant());
        newPartyTransaction.setTransactionDate(combinedDate);

        PartyTransaction updatedTransaction = partyTransactionService.updateTransaction(newPartyTransaction);
        return "redirect:/users/party/view/" + partyTransaction.getParty().getId();
    }

 @PostMapping("/set-due-date")
@ResponseBody
public ResponseEntity<Map<String, Object>> setDueDate(@RequestBody Map<String, Object> payload) {
    Map<String, Object> response = new HashMap<>();
    try {
        String date = (String) payload.get("date");
        Long partyId = Long.valueOf(payload.get("partyId").toString());
        logger.info("Date: " + date);
        logger.info("Party ID: " + partyId);
        Party party = partyService.getPartyById(partyId);
        Date dueDate = Helper.convertStringToDate(date);
        party.setDueDate(dueDate);
        partyService.updateParty(party);
        response.put("status", "success");
        response.put("message", "Due date updated successfully");
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", e.getMessage());
    }
    return ResponseEntity.ok(response);
}

    private Party buildPartyFromForm(PartyForm partyForm, Business business) {
        return Party.builder()
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
    }

    private Party buildPartyFromRow(Row row, Business business) {
        return Party.builder()
                .partyType(PartyType.valueOf(row.getCell(0).getStringCellValue()))
                .name(row.getCell(1).getStringCellValue())
                .mobile(String.valueOf((long) row.getCell(2).getNumericCellValue()))
                .gstIN(row.getCell(5).getStringCellValue())
                .houseNumber(row.getCell(6).getStringCellValue())
                .area(row.getCell(7).getStringCellValue())
                .city(row.getCell(9).getStringCellValue())
                .state(row.getCell(10).getStringCellValue())
                .pincode(String.valueOf((int) row.getCell(8).getNumericCellValue()))
                .business(business)
                .balance(0d)
                .build();
    }

    private void createOpeningBalanceTransaction(PartyForm partyForm, Party party) {
        Double balance = partyForm.getBalance();
        if (balance != null && balance != 0) {
            PartyTransaction transaction = PartyTransaction.builder()
                    .amount(balance)
                    .party(party)
                    .transactionType(partyForm.getTransectionType().equals("you-gave") ? TransactionType.CREDIT : TransactionType.DEBIT)
                    .description("Opening Balance")
                    .build();
            partyTransactionService.createTransaction(transaction);
        }
    }

    private void updatePartyDetails(Party partyFromDB, Party party) {
        partyFromDB.setName(party.getName());
        partyFromDB.setMobile(party.getMobile());
        partyFromDB.setPartyType(party.getPartyType());
        partyFromDB.setHouseNumber(party.getHouseNumber());
        partyFromDB.setArea(party.getArea());
        partyFromDB.setCity(party.getCity());
        partyFromDB.setState(party.getState());
        partyFromDB.setPincode(party.getPincode());
        partyFromDB.setGstIN(party.getGstIN());
    }
}