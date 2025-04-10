package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.pingpongspring.repository.dao.OrderCrudOperations;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class Order {
    private long id;
    private String reference;
    private List<OrderStatus> status;
    private List<DishOrder> dishOrders;

    public StatusOrder getActualStatus(){
        OrderStatus orderStatus = this.getStatus().stream()
                .max(Comparator.comparing(OrderStatus::getDateTime))
                .orElse(new OrderStatus(Instant.now(), StatusOrder.CREATE,this.id));

        return orderStatus.getStatusOrder();
    }

    public StatusOrder getActualStatusByReference(){
        OrderCrudOperations subjectOrder = new OrderCrudOperations();
        Order order = subjectOrder.findByReference(this.reference);

        OrderStatus orderStatus = order.getStatus().stream()
                .max(Comparator.comparing(OrderStatus::getDateTime))
                .orElse(new OrderStatus(Instant.now(), StatusOrder.CREATE,this.id));

        return orderStatus.getStatusOrder();
    }

    public List<DishOrder> getDishesOrdersInDB(){
        OrderCrudOperations subject = new OrderCrudOperations();
        List<DishOrder> dishOrders = subject.getDishByOrder(this.id);

        return dishOrders;
    }

    public Double getTotalAmount(){
        OrderCrudOperations subject = new OrderCrudOperations();
        List<DishOrder> dishOrders = subject.getDishByOrder(this.id);

        return dishOrders.stream()
                .mapToDouble(dishOrder -> dishOrder.getQuantity() * dishOrder.getDish().getPrice())
                .sum();
    }

    public void addDishOrders(List<DishOrder> dishOrders){
        this.setDishOrders(dishOrders);
    }
    public void addOrderStatusList(List<OrderStatus> orderStatuses){
        this.setStatus(orderStatuses);
    }


    public Order(long id, String reference) {
        this.id = id;
        this.reference = reference;
    }
}
