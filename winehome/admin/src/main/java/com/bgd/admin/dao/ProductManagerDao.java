package com.bgd.admin.dao;

import com.bgd.admin.entity.ProductManagerDto;
import com.bgd.admin.entity.param.ProductFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品管理 Dao
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface ProductManagerDao {

    void addProduct(ProductManagerDto productDto);
    void deleteProduct(@Param("productId") Long productId);
    void updateProduct(ProductManagerDto ProductDto);

    Long countProduct(ProductFindParam productFindParam);
    List<ProductManagerDto> findProduct(ProductFindParam productFindParam);

    ProductManagerDto findOneProduct(@Param("productId") Long productId);

}
