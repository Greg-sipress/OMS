package org.example.matchingengine.handlers;

import org.example.matchingengine.exceptions.InvalidOrderException;
import org.example.matchingengine.model.TradeOrder;

public interface OrderValidator {
    void validate(TradeOrder order) throws InvalidOrderException;
}
