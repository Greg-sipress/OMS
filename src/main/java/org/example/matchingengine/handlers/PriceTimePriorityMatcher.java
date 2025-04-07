package org.example.matchingengine.handlers;

import org.example.matchingengine.exceptions.MatchingException;
import org.example.matchingengine.model.Trade;
import org.example.matchingengine.model.TradeOrder;
import org.example.matchingengine.service.OrderBookManager;

import java.util.ArrayList;
import java.util.List;

public class PriceTimePriorityMatcher implements OrderMatcher{
    private final OrderBookManager orderBook;

    public List<Trade> trades = new ArrayList<>();
    public PriceTimePriorityMatcher(OrderBookManager orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void matchOrder(TradeOrder order) throws MatchingException {
       orderBook.addOrder(order);
       trades.addAll(orderBook.matchOrders());
    }
}
