package org.example.matchingengine.service;

import org.example.matchingengine.helpers.TriangularDistribution;
import org.example.matchingengine.model.Trade;
import org.example.matchingengine.model.TradeOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class OrderBookManager {
    private static final int MAX_PRICE = 2000; // Maximum allowed price level
    private static final int MIN_PRICE = 0; // Minimum allowed price level
    private final int basePrice; // Base price for mapping indices
    private final AtomicReferenceArray<List<TradeOrder>> bids; // Buying orders (indexed by price)
    private final AtomicReferenceArray<List<TradeOrder>> offers; // Selling orders (indexed by price)

    public OrderBookManager(int basePrice) {
        this.basePrice = basePrice;
        // AtomicReferenceArrays sized for the price range
        int size = MAX_PRICE - MIN_PRICE + 1;
        this.bids = new AtomicReferenceArray<>(size); // Array for buy orders
        this.offers = new AtomicReferenceArray<>(size); // Array for sell orders
    }

    // Adds a new order (bid or offer) to the order book
    public void addOrder(TradeOrder order) {
        int index = calculateIndex(order.getPrice());
        if (index < 0 || index >= bids.length()) {
            throw new IllegalArgumentException("Price out of bounds: " + order.getPrice());
        }

        // Determine if this is a bid or offer
        AtomicReferenceArray<List<TradeOrder>> array = order.getSide().equalsIgnoreCase("BUY") ? bids : offers;

        // Ensure thread-safe addition
        array.updateAndGet(index, currentOrders -> {
            if (currentOrders == null) {
                currentOrders = new ArrayList<>();
            }
            currentOrders.add(order);
            return currentOrders;
        });
    }

    // Removes an order from the order book (either from bids or offers)
    public boolean removeOrder(TradeOrder order) {
        int index = calculateIndex(order.getPrice());
        if (index < 0 || index >= bids.length()) {
            throw new IllegalArgumentException("Price out of bounds: " + order.getPrice());
        }

        // Determine whether this is a bid or an offer
        AtomicReferenceArray<List<TradeOrder>> array = order.getSide().equalsIgnoreCase("BUY") ? bids : offers;

        // Safely remove the order
        return array.updateAndGet(index, currentOrders -> {
            if (currentOrders != null) {
                currentOrders.remove(order);
                // Return null if the list becomes empty
                return currentOrders.isEmpty() ? null : currentOrders;
            }
            return null;
        }) != null;
    }

    // Matches orders and creates trades
    public List<Trade> matchOrders() {
        List<Trade> trades = new ArrayList<>();

        int bidIndex = bids.length() - 1; // Start from the highest bid
        int offerIndex = 0; // Start from the lowest offer

        // Iterate over overlapping indices for bids and offers
        while (bidIndex >= 0 && offerIndex < offers.length()) {
            List<TradeOrder> bidOrders = bids.get(bidIndex);
            List<TradeOrder> offerOrders = offers.get(offerIndex);

            if (bidOrders == null) {
                bidIndex--;
                continue;
            }
            if (offerOrders == null) {
                offerIndex++;
                continue;
            }

            // Match bid orders with offer orders at overlapping price points
            while (!bidOrders.isEmpty() && !offerOrders.isEmpty()) {
                TradeOrder buyer = bidOrders.get(0);
                TradeOrder seller = offerOrders.get(0);

                if (buyer.getPrice() >= seller.getPrice()) {
                    // Price match: create a trade
                    int quantity = Math.min(buyer.getQuantity(), seller.getQuantity());


                    trades.add(new Trade.Builder()
                            .withPrice(buyer.getPrice())
                            .withQuantity(quantity)
                            .withBuyerId(buyer.getOrderId())
                                    .withSellerId(seller.getOrderId()).build());
                    // Adjust quantities or remove fully matched orders
                    if (buyer.getQuantity() > quantity) {
                        buyer.setQuantity(buyer.getQuantity() - quantity);
                    } else {
                        bidOrders.remove(0);
                    }

                    if (seller.getQuantity() > quantity) {
                        seller.setQuantity(seller.getQuantity() - quantity);
                    } else {
                        offerOrders.remove(0);
                    }
                } else {
                    // No more matching possible between these levels
                    break;
                }
            }

            // Remove empty lists
            if (bidOrders.isEmpty()) {
                bids.set(bidIndex, null);
                bidIndex--;
            }
            if (offerOrders.isEmpty()) {
                offers.set(offerIndex, null);
                offerIndex++;
            }
        }

        return trades;
    }

    // Helper: Calculate the index for the given price
    private int calculateIndex(double price) {
        return (int) (price - basePrice);
    }

    // Get the best bid price (highest buy price)
    public Double getBestBid() {
        for (int i = bids.length() - 1; i >= 0; i--) {
            if (bids.get(i) != null) {
                return (double) (basePrice + i); // Map back to price
            }
        }
        return null; // No bids
    }

    // Get the best offer price (lowest sell price)
    public Double getBestOffer() {
        for (int i = 0; i < offers.length(); i++) {
            if (offers.get(i) != null) {
                return (double) (basePrice + i); // Map back to price
            }
        }
        return null; // No offers
    }


}