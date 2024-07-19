package com.generator.facade.impl;

import com.generator.facade.IArrangeApiInformFacade;
import com.generator.facade.bo.ApiDetailOutput;
import com.generator.facade.bo.ControllerApiDetailOutput;
import com.generator.model.ApiDetail;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArrangeApiInformFacadeImpl implements IArrangeApiInformFacade {
    private final String[] httpMethodArr = {"post", "get", "put", "patch", "delete"};
    @Override
    public List<ControllerApiDetailOutput> getControllerApiDetailList(String path) {
        List<ControllerApiDetailOutput> controllerApiDetailOutputList = new ArrayList<>();
        try (InputStream inputStream = new URL("file:" + path).openStream();
             JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {
            JsonObject jsonObject = jsonReader.readObject();

            JsonObject pathsObject = jsonObject.getJsonObject("paths");
            if (pathsObject != null) {
                for (String url : pathsObject.keySet()) {
                    System.out.println("API路徑: " + url);
                    // 為每個路徑名稱獲取對應的路徑物件
                    JsonObject urlObj = pathsObject.getJsonObject(url);
                    // 以該api的http method作為key
                    JsonObject httpMethodObj = this.getHttpMethodObj(urlObj);
                    // 直接獲取 "tags" 物件的key集合
                    Set<String> keySet = httpMethodObj.keySet();
                    String controllerName = null;
                    String description = null;
                    for (String key : keySet) {
                        if ("tags".equals(key)) {
                            JsonValue value = httpMethodObj.get("tags");
                            JsonArray jsonArray = (JsonArray) value;
                            controllerName = jsonArray.get(0).toString();
                            // 轉駝峰式
                            controllerName = this.toCamelCase(controllerName);
                            controllerName = this.replaceQuote(controllerName);
                            System.out.println("controller: " + controllerName);
                        } else if ("description".equals(key)) {
                            JsonValue value = httpMethodObj.get("description");
                            description = value.toString();
                            description = this.replaceQuote(description);
                            System.out.println("description: " + description);
                        }
                    }
                    ControllerApiDetailOutput output = new ControllerApiDetailOutput();
                    output.setCtrlName(controllerName);
                    output.setApiUrl(url);
                    output.setApiDescription(description);
                    controllerApiDetailOutputList.add(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return controllerApiDetailOutputList;
    }

    private JsonObject getHttpMethodObj(JsonObject urlObj) {
        JsonObject httpMethodObj = null;
        for (String httpMethod : httpMethodArr) {
            httpMethodObj = urlObj.getJsonObject(httpMethod);
            if (httpMethodObj != null) {
                break;
            }
        }
        return httpMethodObj;
    }

    // 轉為駝峰式
    public String toCamelCase(String value) {
        value = value.replace("-", " ");
        // 分割單詞
        String[] parts = value.split(" ");
        // 第一個單詞字母開頭改大寫
        StringBuilder camelCaseStringBuilder = new StringBuilder(parts[0].toUpperCase());
        for (int i = 1; i < parts.length; i++) {
            // 從第二個單詞開始找到字母開頭並改大寫
            camelCaseStringBuilder.append(parts[i].substring(0, 1).toUpperCase()); // 首字母大寫
            camelCaseStringBuilder.append(parts[i].substring(1).toLowerCase()); // 其他字母小寫
        }
        return camelCaseStringBuilder.toString();
    }

    private String replaceQuote(String string) {
        return string.replace("\"", "");
    }

    // 整理物件階層，相同controller的api放在同個物件
    @Override
    public List<ApiDetailOutput> formatToApiDetail(List<ControllerApiDetailOutput> controllerApiDetailOutputList) {
        Map<String, List<ApiDetail>> apiMap = new HashMap<>();
        for (ControllerApiDetailOutput output : controllerApiDetailOutputList) {
            String ctrlName = output.getCtrlName();
            String apiUrl = output.getApiUrl();
            String apiDescription = output.getApiDescription();

            List<ApiDetail> apiDetailList;
            if (apiMap.get(ctrlName) == null) {
                apiDetailList = new ArrayList<>();
            } else {
                apiDetailList = apiMap.get(ctrlName);
            }

            ApiDetail apiDetail = new ApiDetail();
            apiDetail.setApiUrl(apiUrl);
            apiDetail.setApiDescription(apiDescription);
            apiDetailList.add(apiDetail);

            apiMap.put(ctrlName, apiDetailList);
        }

        List<ApiDetailOutput> apiDetailOutputList = null;
        if (!apiMap.isEmpty()) {
            apiDetailOutputList = apiMap.entrySet().stream()
                    .map(api -> new ApiDetailOutput(api.getKey(), api.getValue()))
                    .collect(Collectors.toList());
        }

        return apiDetailOutputList;
    }
}
