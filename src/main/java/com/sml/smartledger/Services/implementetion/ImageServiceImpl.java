package com.sml.smartledger.Services.implementetion;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.sml.smartledger.Helper.AppConstants;
import com.sml.smartledger.Services.interfaces.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {

        try {
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            var uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", fileName));
            return this.getUrlFromPublicId(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary.url().transformation(
                new com.cloudinary.Transformation().width(AppConstants.CONTACT_IMAGE_WIDTH).height(AppConstants.CONTACT_IMAGE_HEIGHT).crop(AppConstants.CONTACT_IMAGE_CROP)
        ).generate(publicId);
    }
}
