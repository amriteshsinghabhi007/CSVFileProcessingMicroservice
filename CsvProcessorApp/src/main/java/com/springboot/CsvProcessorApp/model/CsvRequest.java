package com.springboot.CsvProcessorApp.model;

import lombok.Data;

@Data
public class CsvRequest {
    private String filePath;
    private String fileName;
}
