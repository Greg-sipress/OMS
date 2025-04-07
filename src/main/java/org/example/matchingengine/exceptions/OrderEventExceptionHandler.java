package org.example.matchingengine.exceptions;

import com.lmax.disruptor.ExceptionHandler;
import org.example.matchingengine.model.OrderEvent;
import org.example.matchingengine.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.MeterRegistry;


public class OrderEventExceptionHandler implements ExceptionHandler<OrderEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MeterRegistry metrics;

    public OrderEventExceptionHandler(MeterRegistry metrics) {
        this.metrics = metrics;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, OrderEvent event) {
        metrics.counter("event.errors").increment();
        logger.error("Error processing event {}: {}", sequence, event, ex);

        if (event != null) {
            event.setStatus(OrderStatus.FAILED);
            event.setProcessingError(ex);
        }
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("Exception during disruptor start", ex);
        metrics.counter("startup.errors").increment();
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("Exception during disruptor shutdown", ex);
        metrics.counter("shutdown.errors").increment();
    }
}