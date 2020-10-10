package com.techyourchance.testdrivendevelopment.exercise7;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/*
The three rules:
1) You are not allowed to write any production code unless it is to make a failing unit test pass
2) You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures
3) You are not allowed to write any more production code than is sufficient to pass the one failing unit test
 */

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

	//	1) If the server request completes successfully,
	//	then use case should indicate successful completion of the flow.
	// 2) If the server request completes successfully, then the fetched reputation should be returned
	//  3) If the server request fails for any reason, failure returned
	//	4) If the server request fails for any reason, the returned reputation should be 0.
}