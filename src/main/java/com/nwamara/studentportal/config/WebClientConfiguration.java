/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nwamara.studentportal.config;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

/**
 *
 * @author Seun Owa
 */
@Slf4j
@Configuration
public class WebClientConfiguration {
    
    @Bean
    public WebClient webClient(){
        
        HttpClient httpClient = HttpClient
            .create()
            //.wiretap(true);
            .wiretap("reactor.netty.http.client.HttpClient",  LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        
        WebClient webClient = WebClient
                .builder()
                //.baseUrl(fetchApiHostName(request.getFinancialInstuitutionCode()))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest2())
                .filter(logResposneStatus())
                .build();
        
        return webClient;
    }
    
    private ExchangeFilterFunction logRequest2() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            StringBuilder logStr = new StringBuilder();
            logStr.append("Request:\n")
            .append("\nURI: ")
            .append(clientRequest.url())
            .append("\nMethod: ")
            .append(clientRequest.method())
            .append("\nHeaders:\n");
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logStr.append(String.format("%s : %s" , name, value))));
            //clientRequest.body().insert(clientRequest, cntxt);
            
            log.info(logStr.toString());
//            LOGGER.info("Request: {} {}", clientRequest.method(), clientRequest.url());
//            clientRequest.headers()
//                    .forEach((name, values) -> values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
    
    private ExchangeFilterFunction logResposneStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
