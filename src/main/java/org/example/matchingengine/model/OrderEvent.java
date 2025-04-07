package org.example.matchingengine.model;


public class OrderEvent {
    private volatile TradeOrder order;
    private volatile OrderStatus status;
    private long timestamp;
    private Throwable processingError;

    // Reset for object reuse
    public void clear() {
        this.order = null;
        this.status = null;
        this.timestamp = 0;
        this.processingError = null;
    }

    // Getters and setters omitted for brevity
    public TradeOrder getOrder() {
        return order;
    }

    public void setOrder(TradeOrder order) {
        this.order = order;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Throwable getProcessingError() {
        return processingError;
    }

    public void setProcessingError(Throwable processingError) {
        this.processingError = processingError;
    }

}

