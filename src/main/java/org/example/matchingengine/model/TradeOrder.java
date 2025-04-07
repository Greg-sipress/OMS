package org.example.matchingengine.model;

import jakarta.persistence.Entity;
// import lombok.Data;  // Remove this import as we are explicitly writing getters and setters now.


// @Data  // Remove this annotation as we are writing explicit getter and setter methods.
@Entity
public class TradeOrder {


    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int orderId;
    private double price;
    private int quantity;
    private long timestamp;
    private String side; // "BUY" or "SELL"
    private String symbol;
    private String marketLimit;


    public TradeOrder() {}


    private TradeOrder(Builder builder) {
        this.orderId = builder.orderId;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.timestamp = builder.timestamp;
        this.side = builder.side;
        this.symbol = builder.symbol;
        this.marketLimit = builder.marketLimit;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMarketLimit() {
        return marketLimit;
    }

    public void setMarketLimit(String marketLimit) {
        this.marketLimit = marketLimit;
    }
    public String toString() {

        return toJson();

    }

    public String toJson() {
        return String.format(
                "{\"orderId\":%d,\"price\":%.2f,\"quantity\":%d,\"timestamp\":\"%s\",\"side\":\"%s\",\"symbol\":\"%s\",\"marketLimit\":\"%s\"}",
                orderId, price, quantity, timestamp, side, symbol, marketLimit
        );
    }


    public static class Builder {
        private int orderId;
        private double price;
        private int quantity;
        private long timestamp;
        private String side; // "BUY" or "SELL"
        private String symbol;
        private String marketLimit;

        public Builder orderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder price(double price) {
            this.price = Math.round(price * 20.0) / 20.0;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder side(String side) {
            this.side = side;
            return this;
        }
        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public Builder marketLimit(String marketLimit) {
            this.marketLimit = marketLimit;
            return this;
        }

        public TradeOrder build() {
            validate(); // Validation before building
            return new TradeOrder(this );
        }



        private void validate() {
            if (orderId == 0) {
                throw new IllegalArgumentException("Order ID cannot be null or empty");
            }
            if (!"BUY".equals(side) && !"SELL".equals(side)) {
                throw new IllegalArgumentException("Side must be either 'BUY' or 'SELL'");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
        }
    }


}
