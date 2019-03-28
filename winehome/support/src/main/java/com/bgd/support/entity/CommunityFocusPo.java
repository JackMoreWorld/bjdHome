package com.bgd.support.entity;

import com.bgd.support.base.BasePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityFocusPo extends BasePo {
    private Long vipId;
    private String focus;


}
