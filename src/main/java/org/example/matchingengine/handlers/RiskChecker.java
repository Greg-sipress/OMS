package org.example.matchingengine.handlers;

import com.lmax.disruptor.EventHandler;
import org.example.matchingengine.engine.RiskEngine;
import org.example.matchingengine.model.OrderEvent;

public class RiskChecker implements EventHandler<OrderEvent> {

    RiskEngine riskEngine = new RiskEngine();
    public RiskChecker() {
    }


    @Override
    public void onEvent(OrderEvent order, long sequence, boolean endOfBatch) {
        if (!riskEngine.allows(order.getOrder())) {

            throw new RuntimeException("Risk check failed");
        }
    }
}
