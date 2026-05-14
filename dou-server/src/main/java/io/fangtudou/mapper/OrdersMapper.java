package io.fangtudou.mapper;

import com.github.pagehelper.Page;
import io.fangtudou.dto.OrdersPageQueryDTO;
import io.fangtudou.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrdersMapper {

    @Insert("insert into orders (number, status, table_number, amount, remark, order_time) " +
            "values (#{number}, #{status}, #{tableNumber}, #{amount}, #{remark}, #{orderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO dto);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    void update(Orders orders);
}