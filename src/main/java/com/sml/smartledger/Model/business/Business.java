package com.sml.smartledger.Model.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sml.smartledger.Model.BaseModel;
import com.sml.smartledger.Model.User;
import com.sml.smartledger.Model.bill.Bill;
import com.sml.smartledger.Model.bill.CustomFields;
import com.sml.smartledger.Model.inventory.Product;
import com.sml.smartledger.Model.inventory.Service;
import com.sml.smartledger.Model.bill.Expanses;
import com.sml.smartledger.Model.party.Party;
import com.sml.smartledger.Model.staff.StaffMember;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "business")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Business extends BaseModel implements Serializable {
    String name;
    String mobile;
    String address;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<StaffMember> staffList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Party> parties = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    List<Bill> bills = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    List<Expanses> expansesList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Product> products = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Service> services = new ArrayList<>();

    @ElementCollection
    List<String> customFields = new ArrayList<>();
    private String logo;

    double totalCredit;
    double totalDebit;
    int totalServicesSold;
    int totalServiceAmount;
    int totalProductsStock = 0;
    int lowStockProducts;

    @Override
    public String toString() {
        return "Business{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", staffList=" + staffList.size() +
                ", parties=" + parties.size() +
                ", bills=" + bills.size() +
                ", expansesList=" + expansesList.size() +
                ", products=" + products.size() +
                ", services=" + services.size() +
                ", logo='" + logo + '\'' +
                ", totalCredit=" + totalCredit +
                ", totalDebit=" + totalDebit +
                '}';
    }
}