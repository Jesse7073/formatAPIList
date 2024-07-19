package com.generator.facade.bo;

import com.generator.model.ApiDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiDetailOutput {
    // controller名稱
    private String ctrlName;

    // api資訊
    private List<ApiDetail> apiDetailList;
}
