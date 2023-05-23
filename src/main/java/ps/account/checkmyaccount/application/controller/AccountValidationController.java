package ps.account.checkmyaccount.application.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.account.checkmyaccount.domain.servie.FileProcessorService;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
public class AccountValidationController {
    @Autowired
    private FileProcessorService fileProcessorService;
    @GetMapping("/xsxl")
    public void checkAccount() throws IOException, InvalidFormatException {
        System.out.println();
        String path = "/home/pradeep/Documents/Personal Projects/checkmyaccount/src/main/resources/input/testFile.xlsx";
        FileInputStream is = new FileInputStream(path);
        OPCPackage opcPackage = OPCPackage.open(is);
        XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(opcPackage);
        XSSFSheet sheet = workbook.getSheetAt(0);

        fileProcessorService.validateExcelFile(sheet);

        opcPackage.close();
        workbook.close();
        is.close();


    }

}