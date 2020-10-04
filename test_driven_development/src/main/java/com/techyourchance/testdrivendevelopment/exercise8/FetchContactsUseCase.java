package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

	private final GetContactsHttpEndpoint getContactsHttpEndpoint;
	private final List<Listener> listeners = new ArrayList<>();

	public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
		this.getContactsHttpEndpoint = getContactsHttpEndpoint;
	}

	public void fetchContactAndNotify() {
		getContactsHttpEndpoint.getContacts("", new GetContactsHttpEndpoint.Callback() {
			@Override
			public void onGetContactsSucceeded(List<ContactSchema> cartItems) {
				for (Listener listener : listeners) {
					listener.onContactsFetched(null);
				}
			}

			@Override
			public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {

			}
		});
	}

	public void registerListener(Listener listener) {
		listeners.add(listener);
	}

	public interface Listener {
		void onContactsFetched(List<Contact> contacts);
	}
}
