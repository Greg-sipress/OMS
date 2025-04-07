# OMS


Main file is called MathcingMain.java. It starts the process. 

Core file is the OrderProcessor, it sets up the event driven steps, what is don't in paralled and what is done sequentially

I've implemented a few event handlers : Validator, Matcher, Logger, RiskValidator, Monitor, OrderSender

About the Order Book Manager itself, the book consists of an atomic array of sets of trade Orders. The default matching algorithm is price/time.

The order sender is a Kernel bypass, encoding the tradeorder and putting it in a UDP packet. Saves time  not having to negotiate the TCP handshake. 

