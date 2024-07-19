package com.generator.facade;

import com.generator.facade.bo.ApiDetailOutput;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IExportFileFacade {
    public void exportExcel(List<ApiDetailOutput> apiDetailOutputList, String absolutePath);
}
