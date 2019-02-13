package com.takeaway.happyemployee.model.persistence;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullName {
    @ApiModelProperty(notes = "Employee's first name")
    String firstName;
    @ApiModelProperty(notes = "Employee's last name")
    String lastName;
}
