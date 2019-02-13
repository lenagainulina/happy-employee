package com.takeaway.happyemployee.model.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FullNameConverter implements AttributeConverter<FullName, String> {

    private static final String SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(FullName fullName) {
        if (fullName == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (fullName.getFirstName() != null && !fullName.getFirstName().isEmpty()) {
            sb.append(fullName.getFirstName());
            sb.append(SEPARATOR);
        }

        if (fullName.getLastName() != null && !fullName.getLastName().isEmpty()) {
            sb.append(fullName.getLastName());
        }

        return sb.toString();
    }

    @Override
    public FullName convertToEntityAttribute(String dbPersonName) {
        if (dbPersonName == null) {
            return null;
        }

        if (dbPersonName.isEmpty()) {
            return new FullName();
        }

        String[] pieces = dbPersonName.split(SEPARATOR);
        FullName personName = new FullName();
        personName.setFirstName(pieces[0]);
        personName.setLastName(pieces.length > 1 ? pieces[1] : "");
        return personName;
    }
}

