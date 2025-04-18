package school.hei.pingpongspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.pingpongspring.repository.bd.DataSource;
import school.hei.pingpongspring.repository.dao.OrderDAO;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class Order {
    private long id;
    private String reference;
    @JsonIgnore
    private List<OrderStatus> status;
    private List<DishOrder> dishOrders = new ArrayList<>();

    public Order(String reference) {
        this.reference = reference;
    }

    public StatusOrder getActualStatus() {
        if (this.getStatus() == null || this.getStatus().isEmpty()) {
            return new OrderStatus(Instant.now(), StatusOrder.CREATE, this.id).getStatusOrder();
        }

        OrderStatus orderStatus = this.getStatus().stream()
                .max(Comparator.comparing(OrderStatus::getDateTime))
                .orElse(new OrderStatus(Instant.now(), StatusOrder.CREATE, this.id));

        return orderStatus.getStatusOrder();
    }

//    public StatusOrder getActualStatusByReference(){
//        OrderDAO subjectOrder = new OrderDAO();
//        Order order = subjectOrder.findByReference(this.reference);
//
//        OrderStatus orderStatus = order.getStatus().stream()
//                .max(Comparator.comparing(OrderStatus::getDateTime))
//                .orElse(new OrderStatus(Instant.now(), StatusOrder.CREATE,this.id));
//
//        return orderStatus.getStatusOrder();
//    }
//
//    public List<DishOrder> getDishesOrdersInDB(){
//        final OrderDAO subject = new OrderDAO();
//        List<DishOrder> dishOrders = subject.getDishByOrder(this.id);
//
//        return dishOrders;
//    }
//
//    public Double getTotalAmount(){
//        OrderDAO subject = new OrderDAO();
//        List<DishOrder> dishOrders = subject.getDishByOrder(this.id);
//
//        return dishOrders.stream()
//                .mapToDouble(dishOrder -> dishOrder.getQuantity() * dishOrder.getDish().getPrice())
//                .sum();
//    }

    public Duration DurationDishOrder(long dishOrdersId){

            Optional<DishOrder> dishOrder = this.dishOrders.stream()
                    .filter(d -> d.getId() == dishOrdersId)
                    .findFirst();

            List<DishOrderStatus> statuses = dishOrder.get().getStatus();

            Optional<DishOrderStatus> statusInProgress = statuses.stream()
                    .filter(s -> s.getStatusOrder() == StatusOrder.IN_PROGRESS)
                    .findFirst();

            Optional<DishOrderStatus> statusFinished = statuses.stream()
                    .filter(s -> s.getStatusOrder() == StatusOrder.FINISHED)
                    .findFirst();


            return Duration.between(
                    statusInProgress.get().getDateTime(),
                    statusFinished.get().getDateTime()
            );

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
