package com.nwamara.studentportal.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class StudentInvoiceResponseDto {
    private String id;
    private String invoiceReference;
    private String paymentItemId;
    private Double amount;
}
