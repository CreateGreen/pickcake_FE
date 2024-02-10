package co.pickcake.reservedomain.searchcake.dto;

import co.pickcake.imagedomain.entity.ImageFile;
import lombok.Data;

@Data
public class ImageFileDetailsDto {

    private String storePath;

    private Integer seq;
    public ImageFileDetailsDto(ImageFile imageFile) {
        storePath = imageFile.getStoreFileName();
    }
}
