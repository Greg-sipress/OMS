package org.example.matchingengine.components;


import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import org.example.matchingengine.dataStore.OrderEventPersister;
import org.example.matchingengine.dataStore.OrderLogger;
import org.example.matchingengine.exceptions.OrderEventExceptionHandler;
import org.example.matchingengine.handlers.*;
import org.example.matchingengine.model.OrderEvent;
import com.lmax.disruptor.dsl.Disruptor;
import org.example.matchingengine.model.TradeOrder;
import org.example.matchingengine.service.OrderEventProducer;


public class OrderProcessor {
    private final Disruptor<OrderEvent> disruptor;
    private final OrderEventProducer producer;

    // Configuration constants
    private static final WaitStrategy WAIT_STRATEGY = new LiteBlockingWaitStrategy();
    private static final WaitStrategy WAIT_STRATEGY_2 = new BusySpinWaitStrategy();
    private static final int RING_BUFFER_SIZE = 2048; //1024;  //2048, 4096


    public OrderProcessor(OrderValidator validator,
                          OrderMatcher matcher,
                          OrderEventPersister persister) {

        // Initialize Disruptor
        this.disruptor = new Disruptor<>(
                OrderEvent::new,
                RING_BUFFER_SIZE,
                new NamedThreadFactory("order-processor"),
                ProducerType.MULTI, // Multiple producers if needed
                WAIT_STRATEGY
        );


        // Parallel: Validator and Risk run independently
        EventHandlerGroup<OrderEvent> parallelHandlers = disruptor.handleEventsWith(
                new OrderEventValidator(),
                new RiskChecker()

        );
        // Setup processing pipeline
        parallelHandlers.handleEventsWith(new OrderEventMatcher(matcher))
                //.then(new OrderEventRouter())
                .then(new OrderLogger());
                //.then(new RedisEventOrderPersister(new SimpleMeterRegistry()));

        // Set up exception handler
        disruptor.setDefaultExceptionHandler(new OrderEventExceptionHandler(new SimpleMeterRegistry()));

        this.producer = new OrderEventProducer(disruptor.getRingBuffer());
    }

    public void start() {
        disruptor.start();
    }

    public void shutdown() {
        disruptor.shutdown();
    }

    public void processOrder(TradeOrder order) {
        producer.onData(order);
    }
}