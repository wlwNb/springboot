package wlw.zc.demo.system.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFile {
    private MultipartFile file;
    private String userName;
    private String password;
}
