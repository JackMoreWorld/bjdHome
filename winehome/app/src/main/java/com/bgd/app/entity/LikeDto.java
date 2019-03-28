package com.bgd.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {

    private Long actId;
    private Long proId;
    private Integer step; // -1取消  1喜欢


}
