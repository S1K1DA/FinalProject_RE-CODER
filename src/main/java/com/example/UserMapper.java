package com.example;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserByIdTest(@Param("id") Long id);
}
