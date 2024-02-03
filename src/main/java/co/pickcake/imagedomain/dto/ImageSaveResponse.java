package co.pickcake.imagedomain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSaveResponse {

    private String storeName;
    private String ext;
    private HttpStatus status;

}
