package com.bgd.support.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private Long total = 0L;
    private List<T> list;
}
