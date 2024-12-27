package com.sml.smartledger.Forms;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {
    Long id;
    String name;
    double salePrice;
    double purchasePrice;
    int lowStock;
    int openingStock;
    String date;
    String unitType;

    MultipartFile productImage;
    String picture;
}
