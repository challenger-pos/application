package com.fiap.monitoring;

/**
 * Service interface for sending metrics to monitoring systems.
 * This is a pure abstraction without external dependencies.
 */
public interface MetricsService {
	/**
	 * Sends a gauge metric for work order status transition duration.
	 *
	 * @param durationInSeconds The duration in seconds between status transitions
	 * @param statusTag The status tag (e.g., "status:IN_PROGRESS")
	 */
	void sendWorkOrderStatusTransitionDuration(long durationInSeconds, String statusTag);
}
