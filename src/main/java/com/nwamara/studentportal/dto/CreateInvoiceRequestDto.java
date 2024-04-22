package com.nwamara.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CreateInvoiceRequestDto {
    private Double amount;
    private LocalDate dueDate;
    private String type;
    private CreateStudentFinanceAccountRequestDto account;
}

