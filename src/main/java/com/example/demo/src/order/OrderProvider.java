package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.item.model.ItemRes;
import com.example.demo.src.order.model.OrderRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class OrderProvider {

    private final OrderDao orderDao;

    public OrderProvider(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<OrderRes> getOrdersByUser(int userIdx) throws BaseException {
        try{
            List<OrderRes> delivers = orderDao.getDelivers(userIdx);
            System.out.println("print확인 ");

            return delivers;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public OrderRes getOrderByUser(int userIdx, int orderId) throws BaseException {
        try{
            OrderRes order = orderDao.getOrder(userIdx, orderId);

            return order;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<ItemRes> getOrderedItemsByUser(int userIdx) throws BaseException {
        try{
            List<ItemRes> items = orderDao.getOrderedItemsByUser(userIdx);

            return items;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
