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

    @Select("select count(*) from orders where status = #{status} and date(order_time) = curdate()")
    Integer countByStatusToday(Integer status);

    @Select("select count(*) from orders where date(order_time) = curdate()")
    Integer countToday();

    @Select("select coalesce(sum(amount), 0) from orders where status = 2 and date(order_time) = curdate()")
    Double sumTurnoverToday();

    @Select("select count(*) from orders where status = 2 and date(order_time) = curdate()")
    Integer countValidOrdersToday();

    @Select("select count(*) from orders where status = #{status} and order_time >= #{begin} and order_time <= #{end}")
    Integer countByStatusAndDate(@org.apache.ibatis.annotations.Param("status") Integer status,
                                  @org.apache.ibatis.annotations.Param("begin") String begin,
                                  @org.apache.ibatis.annotations.Param("end") String end);

    @Select("select coalesce(sum(amount), 0) from orders where status = 2 and order_time >= #{begin} and order_time <= #{end}")
    Double sumTurnoverByDate(@org.apache.ibatis.annotations.Param("begin") String begin,
                              @org.apache.ibatis.annotations.Param("end") String end);
}