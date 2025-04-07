package org.example.matchingengine;

import org.example.matchingengine.components.OrderProcessor;
import org.example.matchingengine.dataStore.OrderEventPersister;
import org.example.matchingengine.dataStore.OrderLogger;
import org.example.matchingengine.handlers.*;
import org.example.matchingengine.helpers.TriangularDistribution;
import org.example.matchingengine.model.TradeOrder;
import org.example.matchingengine.service.OrderBookManager;

public class MatchingMain {

    OrderBookManager orderBook = new OrderBookManager(100);
    OrderProcessor orderProcessor;

    public MatchingMain() {


        OrderValidator orderValidator = new OrderEventValidator();
        OrderMatcher orderMatcher = new PriceTimePriorityMatcher(orderBook);
        OrderEventPersister orderPersister = new OrderLogger();

        // Create an instance of the OrderProcessor
        orderProcessor = new OrderProcessor(orderValidator, orderMatcher, orderPersister);

        // Start the OrderProcessor
        orderProcessor.start();

        long startTime, endTime=0;
        startTime = System.nanoTime();
        try {

            initializeOrderBook();
            for (int i = 0; i < 400000; i++) {
                orderProcessor.processOrder(generateTradeOrder());
            }

            endTime = System.nanoTime();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred during Order processing.");
        } finally {
            // Shutdown the OrderProcessor


           // System.out.println("duration = " + (endTime-startTime));
            long duration = (endTime-startTime)/1000000;
            System.out.println("duration = " + duration + " ms");
            System.out.println("duration in seconds = " + duration/1000);
            orderProcessor.shutdown();
        }

    }
    public static void main(String[] args) {

        new MatchingMain();
    }

    public void initializeOrderBook() {

        for (int i = 0; i < 10; i++) {
            TradeOrder tradeOrder = generateTradeOrder();
            tradeOrder.setMarketLimit("LIMIT");
            tradeOrder.setSide("BUY");
            double tempPrice = TriangularDistribution.triangular(90, 100,97);
            tradeOrder.setPrice(Math.round(tempPrice * 20.0) / 20.0);

            orderProcessor.processOrder(tradeOrder);

        }
        for (int i = 0; i < 10; i++) {
            TradeOrder tradeOrder = generateTradeOrder();
            tradeOrder.setMarketLimit("LIMIT");
            tradeOrder.setSide("SELL");
            double tempPrice = TriangularDistribution.triangular(100, 110,103);

            tradeOrder.setPrice(Math.round(tempPrice * 20.0) / 20.0);

            orderProcessor.processOrder(tradeOrder);

        }


    }
    public TradeOrder generateTradeOrder() {

        String side = Math.random() > 0.5 ? "BUY" : "SELL";
        String marketLimit = Math.random() > 0.8 ? "MARKET" : "LIMIT";
        double bestBid;
        double bestOffer;
        double tempPriceLimitBuy=50;
        double tempPriceLimitSell=150;


        if (orderBook.getBestBid() == null) {
            bestBid = 90;
        }
        else {
            bestBid = orderBook.getBestBid();
        }

        if (orderBook.getBestOffer() == null) {
            bestOffer = 110;
        }
        else {
            bestOffer = orderBook.getBestOffer();
        }

        if (side.equals("BUY")) {

           // tempPriceLimitBuy = Math.round(TriangularDistribution.triangular(bestBid-10, bestOffer,bestBid) * 20)/20.0 ;
            tempPriceLimitBuy = Math.random()
                    * (bestOffer - bestBid) + bestBid;
            //tempPriceLimitBuy = Math.round(tempPriceLimitBuy * 20) / 20.0;
        }
        else {

            //tempPriceLimitSell = Math.round(TriangularDistribution.triangular(bestBid, bestOffer+10,bestOffer) * 20) /20.0;
            tempPriceLimitSell = Math.random() * (bestOffer - bestBid) + bestBid;
            //tempPriceLimitSell = Math.round(tempPriceLimitSell * 20) / 20.0;

        }

        TradeOrder tradeOrder = new TradeOrder.Builder()
                .orderId(1 + (int) (Math.random() * 100000))
                .quantity(10 + (int) (Math.random() * 100))
                .timestamp(System.currentTimeMillis())
                .side(side)
                .marketLimit(marketLimit)
                .price((side.equals("BUY") && marketLimit.equals("LIMIT")) ? tempPriceLimitBuy: tempPriceLimitSell)
                .symbol("VOO")
                .build();

        return tradeOrder;
    }




}
