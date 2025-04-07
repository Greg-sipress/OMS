package org.example.matchingengine.handlers;

import com.lmax.disruptor.EventHandler;
import org.example.matchingengine.exceptions.InvalidOrderException;
import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.model.OrderStatus;
import org.example.matchingengine.model.TradeOrder;

public class OrderEventValidator implements OrderValidator, EventHandler<OrderEvent> {
    //private final OrderValidator validator;

    public OrderEventValidator() {

    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        try {
            validate(event.getOrder());
            event.setStatus(OrderStatus.VALIDATED);
        } catch (Exception e) {
            event.setStatus(OrderStatus.FAILED);
            event.setProcessingError(e);
        }
    }

    @Override
    public void validate(TradeOrder order) throws InvalidOrderException {

    }
}
