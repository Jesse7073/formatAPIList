package com.generator.facade;

import com.generator.facade.bo.ApiDetailOutput;
import com.generator.facade.bo.ControllerApiDetailOutput;

import java.util.List;

public interface IArrangeApiInformFacade {
    public List<ControllerApiDetailOutput> getControllerApiDetailList(String path);

    public List<ApiDetailOutput> formatToApiDetail(List<ControllerApiDetailOutput> controllerApiDetailOutputList);
}
