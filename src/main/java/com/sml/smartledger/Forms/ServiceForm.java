package com.sml.smartledger.Forms;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceForm {
    Long id;
    String name;
    double servicePrice;
    String date;
    String unitType;

    MultipartFile serviceImage;
    String picture;
}
