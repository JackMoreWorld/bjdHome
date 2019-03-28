package com.bgd.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class ChoiceActDto {

    private String actId;

    private String actImg;

    List<FirstNewProDto> proList;
}
