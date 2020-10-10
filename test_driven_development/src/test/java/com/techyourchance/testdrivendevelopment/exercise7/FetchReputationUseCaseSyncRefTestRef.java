package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/*
The three rules:
1) You are not allowed to write any production code unless it is to make a failing unit test pass
2) You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures
3) You are not allowed to write any more production code than is sufficient to pass the one failing unit test
 */

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncRefTestRef {

	public static final int REPUTATION = 100;

	@Mock
	GetReputationHttpEndpointSync getReputationHttpEndpointSync;

	private FetchReputationUseCaseSyncRef fetchReputationUseCaseSync;

	@Before
	public void setup() {
		fetchReputationUseCaseSync = new FetchReputationUseCaseSyncRef(getReputationHttpEndpointSync);
	}

	//	1) If the server request completes successfully,
	//	then use case should indicate successful completion of the flow.
	// 2) If the server request completes successfully, then the fetched reputation should be returned
	@Test
	public void fetchReputation_success_successReturned() {
		success();

		FetchReputationUseCaseSyncRef.UseCaseResult result = fetchReputationUseCaseSync.fetchReputation();

		assertThat(result.getStatus(), is(FetchReputationUseCaseSyncRef.Status.SUCCESS));
		assertThat(result.getReputation(), is(REPUTATION));
	}

	//  3) If the server request fails for any reason, the use case should indicate that the flow failed.
	@Test
	public void fetchReputation_serverError_failReturned() {
		serverError();

		FetchReputationUseCaseSyncRef.UseCaseResult result = fetchReputationUseCaseSync.fetchReputation();

		assertThat(result.getStatus(), is(FetchReputationUseCaseSyncRef.Status.SERVER_ERROR));
	}

	//	4) If the server request fails for any reason, the returned reputation should be 0.
	@Test
	public void fetchReputation_serverError_reputation0Returned() {
		serverError();

		FetchReputationUseCaseSyncRef.UseCaseResult result = fetchReputationUseCaseSync.fetchReputation();
		assertThat(result.getReputation(), is(0));
	}

	private void success() {
		when(getReputationHttpEndpointSync.getReputationSync())
				.thenReturn(new EndpointResult(EndpointStatus.SUCCESS, REPUTATION));
	}

	private void serverError() {
		when(getReputationHttpEndpointSync.getReputationSync())
				.thenReturn(new EndpointResult(EndpointStatus.SERVER_ERROR, 0));
	}
}