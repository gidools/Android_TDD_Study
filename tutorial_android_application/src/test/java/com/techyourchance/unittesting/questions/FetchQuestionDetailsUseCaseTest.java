package com.techyourchance.unittesting.questions;

import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;
import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase.Listener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FetchQuestionDetailsUseCaseTest {

	public static final String QUESTION_ID = "questionId";
	private FetchQuestionDetailsUseCase SUT;

	@Mock private EndPointTd endPointTd;
	@Mock private Listener listener1;
	@Mock private Listener listener2;

	@Before
	public void setup() {
		SUT = new FetchQuestionDetailsUseCase(endPointTd);
	}

	@Test
	public void fetchQuestionDetail_success_listenersNotifiedWithCorrectData() throws Exception {
		// Arrange
		ArgumentCaptor<QuestionDetails> ac = ArgumentCaptor.forClass(QuestionDetails.class);
		success();
		SUT.registerListener(listener1);
		SUT.registerListener(listener2);
		// Act
		SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
		// Assert
		verify(listener1).onQuestionDetailsFetched(ac.capture());
		verify(listener2).onQuestionDetailsFetched(ac.capture());
		List<QuestionDetails> detailsList = ac.getAllValues();
		assertThat(detailsList.get(0), is(getQuestionDetails()));
		assertThat(detailsList.get(1), is(getQuestionDetails()));
	}

	@Test
	public void fetchQuestionDetail_failure_listenersNotifiedFailure() throws Exception {
		// Arrange
		failure();
		SUT.registerListener(listener1);
		SUT.registerListener(listener2);
		// Act
		SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
		// Assert
		verify(listener1).onQuestionDetailsFetchFailed();
		verify(listener2).onQuestionDetailsFetchFailed();
	}

	private void success() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object [] arguments = invocation.getArguments();
				FetchQuestionDetailsEndpoint.Listener listener =
						(FetchQuestionDetailsEndpoint.Listener) arguments[1];
				listener.onQuestionDetailsFetched(getQuestionSchema());
				return null;
			}
		}).when(endPointTd).fetchQuestionDetails(anyString(), any(FetchQuestionDetailsEndpoint.Listener.class));
	}

	private void failure() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object [] arguments = invocation.getArguments();
				FetchQuestionDetailsEndpoint.Listener listener =
						(FetchQuestionDetailsEndpoint.Listener) arguments[1];
				listener.onQuestionDetailsFetchFailed();
				return null;
			}
		}).when(endPointTd).fetchQuestionDetails(anyString(), any(FetchQuestionDetailsEndpoint.Listener.class));
	}

	private QuestionSchema getQuestionSchema() {
		return new QuestionSchema("title", "id", "body");
	}

	private QuestionDetails getQuestionDetails() {
		return new QuestionDetails("id", "title", "body");
	}

	private static class EndPointTd extends FetchQuestionDetailsEndpoint {

		public EndPointTd() {
			super(null);
		}
	}
}