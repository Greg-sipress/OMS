package org.example.matchingengine.dataStore;

import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.model.TradeOrder;

import com.lmax.disruptor.EventHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OrderLogger implements OrderEventPersister, EventHandler<OrderEvent> {

    int counter = 0;

    @Override
    public void onEvent(OrderEvent orderEvent, long l, boolean b) throws Exception {
        counter++;

        if (counter % 100000 == 0) {
            System.out.println("Counter: " + counter);
            System.out.println("Order: " + orderEvent.getOrder().toString());
        }
        //TradeOrder order = orderEvent.getOrder();
        //System.out.println("Order: " + order.toString());
    }

    @Override
    public void persist(OrderEvent event) {

    }

    @Override
    public CompletableFuture<Void> persistAsync(OrderEvent event) {
        return null;
    }

    @Override
    public void persistBatch(List<OrderEvent> events) {

    }

    @Override
    public List<OrderEvent> getEventsAfter(long timestamp) {
        return List.of();
    }

    @Override
    public long getEventCount(String orderId) {
        return 0;
    }
}
