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
			public void onGetContactsSucceeded(List<ContactSchema> contactSchemas) {
				for (Listener listener : listeners) {
					listener.onContactsFetched(contactsFromContactSchemas(contactSchemas));
				}
			}

			@Override
			public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {

			}
		});
	}

	private List<Contact> contactsFromContactSchemas(List<ContactSchema> contactSchemas) {
		List<Contact> contacts = new ArrayList<>();
		for (ContactSchema schema : contactSchemas) {
			contacts.add(new Contact(schema.getId(), schema.getFullName(), schema.getImageUrl()));
		}
		return contacts;
	}

	public void registerListener(Listener listener) {
		listeners.add(listener);
	}

	public interface Listener {
		void onContactsFetched(List<Contact> contacts);
	}
}
