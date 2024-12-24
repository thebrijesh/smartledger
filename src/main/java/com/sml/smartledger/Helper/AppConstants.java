package com.sml.smartledger.Helper;

import com.sml.smartledger.Model.inventory.UnitModel;
import com.sml.smartledger.Model.inventory.UnitType;

import java.util.List;

public class AppConstants {
    public static final String DEFAULT_ROLE = "ROLE_USER";
    public static final String APP_NAME = "scm";

    public static final int CONTACT_IMAGE_WIDTH = 500;
    public static final int CONTACT_IMAGE_HEIGHT = 500;
    public static final String CONTACT_IMAGE_CROP = "fill";

    public static final int PAGE_SIZE = 10;
    public static final String DEFAULT_PRODUCT_IMAGE_LINK = "https://res.cloudinary.com/dhbcbhr9z/image/upload/v1734935179/1d9b3a87-cc5b-42a7-ab99-9772152b837c.png";

    public static final List<UnitModel> unitList= List.of(new UnitModel("Centimeter", UnitType.CM),
            new UnitModel("Kilometer", UnitType.KM),
            new UnitModel("Pieces", UnitType.PCS),
            new UnitModel("Numbers", UnitType.NOS),
            new UnitModel("Kilogram", UnitType.KG),
            new UnitModel("Gram", UnitType.GMS),
            new UnitModel("Litre", UnitType.LTR),
            new UnitModel("Millilitre", UnitType.ML),
            new UnitModel("Meter", UnitType.MTR),
            new UnitModel("Inch", UnitType.IN),
            new UnitModel("Foot", UnitType.FT),
            new UnitModel("Box", UnitType.BOX),
            new UnitModel("Bottle", UnitType.BTL),
            new UnitModel("Pair", UnitType.PAIR),
            new UnitModel("Dozen", UnitType.DOZ),
            new UnitModel("Day", UnitType.DAY),
            new UnitModel("Month", UnitType.MON),
            new UnitModel("Year", UnitType.YEAR));

}
