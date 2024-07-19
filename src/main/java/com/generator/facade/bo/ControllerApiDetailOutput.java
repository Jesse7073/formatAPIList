package com.generator.facade.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ControllerApiDetailOutput {
    // controller名稱
    private String ctrlName;

    // api路徑
    private String apiUrl;

    // api描述
    private String apiDescription;
}
