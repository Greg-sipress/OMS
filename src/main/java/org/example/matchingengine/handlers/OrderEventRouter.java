package org.example.matchingengine.handlers;

import com.lmax.disruptor.EventHandler;
import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.networking.OrderGateway;

public class OrderEventRouter implements EventHandler<OrderEvent> {


    public OrderGateway orderGateway = new OrderGateway();

    @Override
    public void onEvent(OrderEvent orderEvent, long l, boolean b) throws Exception {
        orderGateway.send(orderEvent.getOrder());
    }
}
