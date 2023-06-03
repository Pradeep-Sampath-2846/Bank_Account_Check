package ps.account.checkmyaccount.application.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ps.account.checkmyaccount.application.dto.request.RequestDto;
import ps.account.checkmyaccount.domain.servie.FileProcessorService;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
public class AccountValidationController {
    @Autowired
    private FileProcessorService fileProcessorService;
    @GetMapping("/xsxl")
    public String checkAccount(@RequestBody RequestDto requestDto) throws IOException, InvalidFormatException {
        String path = "/home/pradeep/Documents/Personal Projects/checkmyaccount/src/main/resources/input/testFile.xlsx";
        FileInputStream is = new FileInputStream(path);
        OPCPackage opcPackage = OPCPackage.open(is);
        XSSFWorkbook workbook = XSSFWorkbookFactory.createWorkbook(opcPackage);
        XSSFSheet sheet = workbook.getSheetAt(0);
        String content;
        try {
           content = fileProcessorService.validateExcelFile(sheet,requestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        opcPackage.close();
        workbook.close();
        is.close();
        return content;


    }

}