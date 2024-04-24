package com.nwamara.studentportal.config;

import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        //setup custom mapping for student metadata
        TypeMap<Student, StudentDto> studentPropertyMapper = modelMapper.createTypeMap(Student.class, StudentDto.class);
        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
