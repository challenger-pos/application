package com.fiap.monitoring;

import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsServiceImpl implements MetricsService{
	private static final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);
	private final StatsDClient statsDClient;

	public MetricsServiceImpl(StatsDClient statsDClient) {
		this.statsDClient = statsDClient;
	}

	@Override
	public void sendWorkOrderStatusTransitionDuration(long durationInSeconds, String statusTag) {
		try {
			statsDClient.gauge("workorder.status.transition.duration", durationInSeconds, statusTag);
		} catch (Exception e) {
			// Log error but don't affect the main flow
			logger.warn("Failed to send Datadog metric for work order status transition: {}", e.getMessage());
		}
	}
}
