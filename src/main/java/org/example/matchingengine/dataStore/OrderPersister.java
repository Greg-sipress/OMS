package org.example.matchingengine.dataStore;


import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.ConcurrencyFailureException;
import org.example.matchingengine.model.TradeOrder;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Persists order data with support for synchronous and asynchronous operations
 */
public interface OrderPersister {

    // ===== Core Operations =====

    /**
     * Persist a single order (synchronous)
     */
    void persist(@NonNull TradeOrder order) throws PersistenceException;

    /**
     * Persist a batch of orders (optimized for bulk inserts)
     */
    void persistBatch(@NonNull List<TradeOrder> orders) throws PersistenceException;

    /**
     * Asynchronously persist an order
     */
    CompletableFuture<Void> persistAsync(@NonNull TradeOrder order);

    /**
     * Retrieve an order by ID
     */
    TradeOrder getOrder(@NonNull String orderId) throws DataRetrievalFailureException;

    // ===== Advanced Operations =====

    /**
     * Atomic compare-and-set operation
     */
    boolean updateIfMatch(@NonNull TradeOrder order, long version) throws ConcurrencyFailureException;

    /**
     * Stream orders updated after given timestamp
     */
    List<TradeOrder> getOrdersModifiedAfter(long timestamp);

    // ===== Status Operations =====

    /**
     * Check if order exists
     */
    boolean exists(@NonNull String orderId);

    /**
     * Get current persistence latency in nanoseconds
     */
    long getCurrentLatency();

    // ===== Lifecycle Methods =====

    /**
     * Initialize connection/resources
     */
    void initialize();

    /**
     * Cleanup resources
     */
    void shutdown();
}