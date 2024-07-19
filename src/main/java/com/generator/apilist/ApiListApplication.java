package com.generator.apilist;

import com.generator.facade.IArrangeApiInformFacade;
import com.generator.facade.IExportFileFacade;
import com.generator.facade.bo.ApiDetailOutput;
import com.generator.facade.bo.ControllerApiDetailOutput;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication(scanBasePackages = {"com.generator"})
public class ApiListApplication implements ApplicationRunner {

	@Autowired
	private IExportFileFacade exportFileFacade;

	@Autowired
	private IArrangeApiInformFacade arrangeApiInformFacade;

	public static void main(String[] args) {
		SpringApplication.run(ApiListApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("請輸入Swagger的api-docs的JSON檔案絕對路徑:");
		String absolutePath = scanner.next();

		System.out.println("輸入的路徑為: " + absolutePath);
		try {
			if (absolutePath == null || absolutePath.isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("未輸入Swagger的api-docs的JSON檔案絕對路徑");
			throw new RuntimeException(e);
		}

		List<ControllerApiDetailOutput> controllerApiDetailOutputList = arrangeApiInformFacade.getControllerApiDetailList(absolutePath);

		List<ApiDetailOutput> apiDetailOutputList = null;
		if (CollectionUtils.isNotEmpty(controllerApiDetailOutputList)) {
			apiDetailOutputList = arrangeApiInformFacade.formatToApiDetail(controllerApiDetailOutputList);
		}

		if (CollectionUtils.isNotEmpty(apiDetailOutputList)) {
			exportFileFacade.exportExcel(apiDetailOutputList, absolutePath);
		}
	}
}
