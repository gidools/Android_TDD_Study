package com.techyourchance.unittesting.questions;

import com.techyourchance.unittesting.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FetchLastActiveQuestionsUseCaseTest {

	private FetchLastActiveQuestionsUseCase SUT;

	private EndPointTd endPointTd;
	@Mock FetchLastActiveQuestionsUseCase.Listener listener1;
	@Mock FetchLastActiveQuestionsUseCase.Listener listener2;
	@Captor ArgumentCaptor<List<Question>> questionListCaptor;

	@Before
	public void setup() {
		endPointTd = new EndPointTd();
		SUT = new FetchLastActiveQuestionsUseCase(endPointTd);
	}

	// success - listeners notified of success with correct data
	@Test
	public void fetchLastActiveQuestion_success_listenersNotifiedWithCorrectData() throws Exception {
		// Arrange
		success();
		SUT.registerListener(listener1);
		SUT.registerListener(listener2);
		// Act
		SUT.fetchLastActiveQuestionsAndNotify();
		// Assert
		verify(listener1).onLastActiveQuestionsFetched(questionListCaptor.capture());
		verify(listener2).onLastActiveQuestionsFetched(questionListCaptor.capture());
		List<List<Question>> questsList = questionListCaptor.getAllValues();
		assertThat(questsList.get(0), is(getExpectedQuestionList()));
		assertThat(questsList.get(1), is(getExpectedQuestionList()));
	}

	// failure - listeners notified of failure
	@Test
	public void fetchLastActiveQuestion_failure_listenersNotifiedFailure() throws Exception {
		// Arrange
		failure();
		SUT.registerListener(listener1);
		SUT.registerListener(listener2);
		// Act
		SUT.fetchLastActiveQuestionsAndNotify();
		// Assert
		verify(listener1).onLastActiveQuestionsFetchFailed();
		verify(listener2).onLastActiveQuestionsFetchFailed();
	}

	private void success() {
		// currently - no operation
	}

	private void failure() {
		endPointTd.isFailure = true;
	}

	private List<Question> getExpectedQuestionList() {
		List<Question> questions = new ArrayList<>();
		questions.add(new Question("id1", "title1"));
		questions.add(new Question("id2", "title2"));
		return questions;
	}

	private static class EndPointTd extends FetchLastActiveQuestionsEndpoint {

		public boolean isFailure;

		public EndPointTd() {
			super(null);
		}

		@Override
		public void fetchLastActiveQuestions(Listener listener) {
			if (isFailure) {
				listener.onQuestionsFetchFailed();
			} else {
				List<QuestionSchema> schemaList = new ArrayList<>();
				schemaList.add(new QuestionSchema("title1", "id1", "body1"));
				schemaList.add(new QuestionSchema("title2", "id2", "body2"));
				listener.onQuestionsFetched(schemaList);
			}
		}
	}
}