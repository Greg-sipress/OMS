package org.example.matchingengine.handlers;

import org.example.matchingengine.exceptions.MatchingException;
import org.example.matchingengine.model.TradeOrder;

public interface OrderMatcher {
    void matchOrder(TradeOrder order) throws MatchingException;
}
