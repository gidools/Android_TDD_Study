package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.FailReason;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseRefTest {

	public static final String ID = "id";
	public static final String FULL_NAME = "fullName";
	public static final String IMAGE_URL = "imageUrl";
	public static final String FULL_PHONE_NUMBER = "01022068301";
	public static final int AGE = 30;
	private FetchContactsUseCaseRef fetchContactsUseCaseRef;

	@Mock private GetContactsHttpEndpoint getContactsHttpEndpointMock;
	@Mock private FetchContactsUseCaseRef.Listener listenerMock1;
	@Mock private FetchContactsUseCaseRef.Listener listenerMock2;

	@Captor	ArgumentCaptor<List<Contact>> acListContact;

	@Before
	public void setUp() throws Exception {
		fetchContactsUseCaseRef = new FetchContactsUseCaseRef(getContactsHttpEndpointMock);
	}

	//1) If the server request completes successfully,
	// then registered listeners should be notified with correct data.
	@Test
	public void fetchContact_success_observersNotifiedWithCorrectData() {
		success();

		fetchContactsUseCaseRef.registerListener(listenerMock1);
		fetchContactsUseCaseRef.registerListener(listenerMock2);

		fetchContactsUseCaseRef.fetchContactAndNotify();

		verify(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));

		verify(listenerMock1).onContactsFetched(acListContact.capture());
		verify(listenerMock2).onContactsFetched(acListContact.capture());

		List<List<Contact>> captures = acListContact.getAllValues();
		List<Contact> capture1 = captures.get(0);
		List<Contact> capture2 = captures.get(1);

		assertThat(capture1, is(getContacts()));
		assertThat(capture2, is(getContacts()));
	}

	//2) If the server request fails for any reason except network error,
	// then registered listeners should be notified about a failure.
	@Test
	public void fetchContact_serverError_observersNotifiedFailure() {
		ArgumentCaptor<FailReason> ac =
				ArgumentCaptor.forClass(FailReason.class);

		generalError();

		fetchContactsUseCaseRef.registerListener(listenerMock1);
		fetchContactsUseCaseRef.registerListener(listenerMock2);

		fetchContactsUseCaseRef.fetchContactAndNotify();

		verify(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));

		verify(listenerMock1).onFetchContactsFailed(ac.capture());
		verify(listenerMock2).onFetchContactsFailed(ac.capture());

		assertThat(ac.getValue(), is(FailReason.GENERAL_ERROR));
	}

	//3) If the server request fails due to network error,
	// then registered listeners should be notified about a network error specifically.
	@Test
	public void fetchContact_networkError_observersNotifiedFailure() {
		ArgumentCaptor<FailReason> ac =
				ArgumentCaptor.forClass(FailReason.class);

		networkError();

		fetchContactsUseCaseRef.registerListener(listenerMock1);
		fetchContactsUseCaseRef.registerListener(listenerMock2);

		fetchContactsUseCaseRef.fetchContactAndNotify();

		verify(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));

		verify(listenerMock1).onFetchContactsFailed(ac.capture());
		verify(listenerMock2).onFetchContactsFailed(ac.capture());

		assertThat(ac.getValue(), is(FailReason.NETWORK_ERROR));
	}

	private void success() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object [] args = invocation.getArguments();
				GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
				callback.onGetContactsSucceeded(getContactSchemas());
				return null;
			}
		}).when(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
	}

	private void generalError() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object [] args = invocation.getArguments();
				GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
				callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
				return null;
			}
		}).when(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
	}

	private void networkError() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object [] args = invocation.getArguments();
				GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
				callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
				return null;
			}
		}).when(getContactsHttpEndpointMock)
				.getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
	}

	private List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<>();
		contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
		return contacts;
	}

	private List<ContactSchema> getContactSchemas() {
		List<ContactSchema> contactSchemas = new ArrayList<>();
		contactSchemas.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
		return contactSchemas;
	}
}