package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    private UpdateUsernameUseCaseSync updateUsernameUseCaseSync;
    private UpdateUsernameHttpEndpointSync usernameHttpEndpointSyncMock;
    private UsersCache usersCacheMock;
    private EventBusPoster eventBusPosterMock;

    @Before
    public void setup() {
        usernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync.class);
        usersCacheMock = mock(UsersCache.class);
        eventBusPosterMock = mock(EventBusPoster.class);

        updateUsernameUseCaseSync = new UpdateUsernameUseCaseSync(
                usernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock);
    }

    // login test : name, pwd passed to UpdateUsernameHttpEndpointSync
    @Test
    public void updateUsername_success_userIdAndNamePassedToEndPoint() throws NetworkErrorException {
        mockLoginSuccess();

        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);

        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        verify(usernameHttpEndpointSyncMock, times(1))
                .updateUsername(ac.capture(), ac.capture());

        List<String> captures = ac.getAllValues();

        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USER_NAME));
    }

    // login success : User cached only once
    @Test
    public void updateUsername_success_userCached() throws NetworkErrorException {
        mockLoginSuccess();
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);

        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        verify(usersCacheMock, times(1))
                .cacheUser(ac.capture());

        User user = ac.getValue();
        assertThat(user.getUserId(), is(USER_ID));
        assertThat(user.getUsername(), is(USER_NAME));
    }

    // login success : EventBusPoster do something only once
    // login fail : User information not cached
    // login fail : EventBusPoster do nothing
    // general error : General error returned
    // server error : Server error returned
    // network error : Network error returned

    private void mockLoginSuccess() throws NetworkErrorException {
        when(usernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult
                        (EndpointResultStatus.SUCCESS, USER_ID, USER_NAME));
    }

}