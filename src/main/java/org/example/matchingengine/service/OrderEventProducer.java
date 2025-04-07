package org.example.matchingengine.service;


import com.lmax.disruptor.RingBuffer;
import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.model.OrderStatus;
import org.example.matchingengine.model.TradeOrder;

public class OrderEventProducer {
    private final RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(TradeOrder order) {
        long sequence = ringBuffer.next();
        try {
            OrderEvent event = ringBuffer.get(sequence);
            event.setOrder(order);
            event.setTimestamp(System.nanoTime());
            event.setStatus(OrderStatus.RECEIVED);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
