package org.example.matchingengine.dataStore;


import com.lmax.disruptor.EventHandler;
import org.example.matchingengine.model.OrderEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface OrderEventPersister  {
    void persist(OrderEvent event);
    CompletableFuture<Void> persistAsync(OrderEvent event);
    void persistBatch(List<OrderEvent> events);
    List<OrderEvent> getEventsAfter(long timestamp);
    long getEventCount(String orderId);
}