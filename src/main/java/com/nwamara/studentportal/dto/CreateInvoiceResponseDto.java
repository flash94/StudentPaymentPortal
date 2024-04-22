package com.nwamara.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateInvoiceResponseDto {
    private String id;
    private String reference;
    private Double amount;
    private String dueDate;
    private String type;
    private String status;
    private String studentId;
    private Links links;
}

