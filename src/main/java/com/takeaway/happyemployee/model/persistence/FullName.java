package com.takeaway.happyemployee.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullName {
    String firstName;
    String lastName;
}
