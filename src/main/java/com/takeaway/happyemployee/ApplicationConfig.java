package com.takeaway.happyemployee;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EntityScan(basePackageClasses = {HappyEmployeeApplication.class})
@EnableJpaAuditing
public class ApplicationConfig  {

}
