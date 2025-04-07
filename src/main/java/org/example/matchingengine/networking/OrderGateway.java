package org.example.matchingengine.networking;


import org.example.matchingengine.model.TradeOrder;
import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.DirectBuffer;

import org.agrona.concurrent.UnsafeBuffer;
import java.nio.ByteBuffer;

public class OrderGateway {
    private final Aeron aeron;
    private final Publication publication;

    public OrderGateway() {
        aeron = Aeron.connect(new Aeron.Context());
        publication = aeron.addPublication("aeron:udp?endpoint=224.0.1.1:40456", 1001);
    }

    private DirectBuffer encodeOrder(TradeOrder order) {
        byte[] bytes = order.toJson().getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        return new UnsafeBuffer(byteBuffer.flip());
    }

    public void send(TradeOrder order) {
        // Encode to SBE (Simple Binary Encoding) format
        DirectBuffer buffer = encodeOrder(order);
        while (publication.offer(buffer) < 0) {
            // Backpressure handling
        }
    }
}
