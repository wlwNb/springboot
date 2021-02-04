package wlw.zc.demo.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Permissions {
    private String id;
    private String permissionsName;
}
