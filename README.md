# formatAPIList
Rearrange Swagger UI's API from JSON object to Excel table.

If your project has installed Swagger UI, you can use this application to rearrange API paths in the JSON object, and export to Excel.

## How to use
* Open the Swagger page of the target project to see API’s resources.
* In the Browser Developer Tools (F12), go to the "Network" panel and find the [api-docs] JSON object , then copy it.
* Paste this JSON object to a text document and save it.
* Run this application, when the message "請輸入Swagger的api-docs的JSON檔案絕對路徑:" shows in the terminal, enter the absolute path of the text document.
* After running the application, the excel file will be exported at the same path.

