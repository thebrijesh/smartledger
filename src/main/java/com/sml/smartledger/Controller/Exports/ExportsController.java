package com.sml.smartledger.Controller.Exports;

import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.business.Business;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.party.PartyTransaction;
import com.sml.smartledger.Services.implementetion.exportServices.ReportGenerationService;
import com.sml.smartledger.Services.interfaces.UserService;
import com.sml.smartledger.Services.interfaces.party.PartyService;
import com.sml.smartledger.Services.interfaces.party.PartyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.sml.smartledger.Helper.Helper.getEmailOfLoggedInUser;

@Controller
@RequestMapping("users/report/export")
public class ExportsController {

    private final PartyTransactionService partyTransactionService;
    private final ReportGenerationService reportGenerationService;
    private final UserService userService;
    private final PartyService partyService;

    @Autowired
    public ExportsController(PartyService partyService , PartyTransactionService partyTransactionService, UserService userService, ReportGenerationService reportGenerationService) {
        this.partyTransactionService = partyTransactionService;
        this.userService = userService;
        this.reportGenerationService = reportGenerationService;
        this.partyService = partyService;
    }


    @GetMapping("/transaction/download")
    public ResponseEntity<InputStreamResource> downloadReport(@RequestParam("format") String format,
                                                              @RequestParam("startDate") String startDateStr,
                                                              @RequestParam("endDate") String endDateStr,
                                                              @RequestParam("term") String term,
                                                              @RequestParam("type") String type,
                                                              Authentication authentication) throws IOException {
        String email = getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);
        Business business = user.getSelectedBusiness();
        List<Party> parties;
        if(type.equals("customer")){
            parties = partyService.getCustomerParties(business.getId());
        }else{
            parties = partyService.getSupplierParties(business.getId());
        }
        List<PartyTransaction> transactions = partyTransactionService.getAllTransactionsByPartyIds(parties);

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Filter transactions based on the date range
        List<PartyTransaction> filteredTransactions = transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransactionDate().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate();
                    return (transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                            (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate)) && transaction.getParty().getName().contains(term);
                })
                .collect(Collectors.toList());

        ByteArrayInputStream bis;
        String fileName;
        String formattedStartDate = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String formatFileName = "transactions_report_" + formattedStartDate + "_to_" + formattedEndDate;
        if ("pdf".equalsIgnoreCase(format)) {
            bis = reportGenerationService.generatePdfReport(filteredTransactions,business.getName());
            fileName =  formatFileName + ".pdf";
        } else {
            bis = reportGenerationService.generateExcelReport(filteredTransactions,business.getName());
            fileName = formatFileName + ".xlsx";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType("pdf".equalsIgnoreCase(format) ? MediaType.APPLICATION_PDF : MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(bis));
    }
}