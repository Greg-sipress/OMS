package org.example.matchingengine.handlers;

import com.lmax.disruptor.EventHandler;
import org.example.matchingengine.exceptions.MatchingException;
import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.model.OrderStatus;
import org.example.matchingengine.model.TradeOrder;


public class OrderEventMatcher implements EventHandler<OrderEvent> {
    private final OrderMatcher matcher; // = new PriceTimePriorityMatcher();

    public OrderEventMatcher(OrderMatcher matcher) {
        this.matcher = matcher;
    }
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        if (event.getStatus() != OrderStatus.VALIDATED) return;

        try {
            matcher.matchOrder(event.getOrder());
            event.setStatus(OrderStatus.MATCHED);
        } catch (Exception e) {
            event.setStatus(OrderStatus.FAILED);
            event.setProcessingError(e);
        }
    }

}
