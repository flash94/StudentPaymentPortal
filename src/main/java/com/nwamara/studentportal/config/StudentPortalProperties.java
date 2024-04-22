package com.nwamara.studentportal.config;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
@Configuration
@ConfigurationProperties(prefix="externalservice")
public class StudentPortalProperties {
    public String financeAccountBaseUrl;
    public String financeInvoicesBaseUrl;

}

