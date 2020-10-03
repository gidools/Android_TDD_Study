package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

	private final FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
	private final UsersCache usersCache;

	public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync,
									UsersCache usersCache) {
		this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
		this.usersCache = usersCache;
	}

	@Override
	public UseCaseResult fetchUserSync(String userId) {
		User cachedUser = usersCache.getUser(userId);
		if (cachedUser != null) {
            return new UseCaseResult(Status.SUCCESS, cachedUser);
		}

		FetchUserHttpEndpointSync.EndpointResult endpointResult;
		try {
			endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId);
		} catch (NetworkErrorException e) {
			return new UseCaseResult(Status.NETWORK_ERROR, null);
		}

		if (endpointResult.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS) {
			User user = new User(endpointResult.getUserId(), endpointResult.getUsername());
			usersCache.cacheUser(user);
			return new UseCaseResult(Status.SUCCESS, user);
		} else {
			return new UseCaseResult(Status.FAILURE, null);
		}
	}
}
