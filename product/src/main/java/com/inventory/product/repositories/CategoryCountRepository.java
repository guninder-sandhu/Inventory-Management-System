package com.inventory.product.repositories;

import com.inventory.product.entities.CategoryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryCountRepository extends JpaRepository<CategoryCount, Integer> {

    @Modifying
    @Query("UPDATE CategoryCount SET categoryCount = :count where id= 1")
    void updateCategoryCount(@Param("count") Integer count);

    @Query("select c.categoryCount from CategoryCount c where c.id=1")
    Integer getCategoryCount();
}
