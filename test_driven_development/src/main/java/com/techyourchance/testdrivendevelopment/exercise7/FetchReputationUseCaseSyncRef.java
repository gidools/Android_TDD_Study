package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;

public class FetchReputationUseCaseSyncRef {

	private final GetReputationHttpEndpointSync getReputationHttpEndpointSync;

	public FetchReputationUseCaseSyncRef(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
		this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
	}

	public UseCaseResult fetchReputation() {
		EndpointResult endpointResult = getReputationHttpEndpointSync.getReputationSync();
		switch (endpointResult.getStatus()) {
			case SUCCESS:
				return new UseCaseResult(Status.SUCCESS, endpointResult.getReputation());
			case SERVER_ERROR:
				return new UseCaseResult(Status.SERVER_ERROR, 0);
			default:
				throw new RuntimeException("Invalid status");
		}
	}

	public enum Status {
		SUCCESS, SERVER_ERROR
	}

	public static class UseCaseResult {

		private final Status status;
		private final int reputation;

		public UseCaseResult(Status status, int reputation) {
			this.status = status;
			this.reputation = reputation;
		}

		public int getReputation() {
			return reputation;
		}

		public Status getStatus() {
			return status;
		}
	}
}
