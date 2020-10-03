package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    private UpdateUsernameUseCaseSync updateUsernameUseCaseSync;

    @Mock private UpdateUsernameHttpEndpointSync usernameHttpEndpointSyncMock;
    @Mock private UsersCache usersCacheMock;
    @Mock private EventBusPoster eventBusPosterMock;

    @Before
    public void setup() {
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
    @Test
    public void updateUsername_success_loginEventPosted() throws Exception {
        // given
        mockLoginSuccess();

        // when
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);

        // then
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        verify(eventBusPosterMock, times(1)).postEvent(ac.capture());

        Object event = ac.getValue();
        assertThat(event, is(instanceOf(UserDetailsChangedEvent.class)));
    }

    // General error. user not cached
    @Test
    public void updateUsername_generalError_userNotCached() throws Exception {
        // given
        mockGeneralError();

        // when
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);

        // then
        verifyNoMoreInteractions(usersCacheMock);
    }

    // auth error : user not cached
    @Test
    public void updateUsername_authError_userNotCached() throws Exception {
        mockAuthError();
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    // server error : user not cached
    @Test
    public void updateUsername_serverError_userNotCached() throws Exception {
        mockServerError();
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    // Success : Success returned
    @Test
    public void updateUsername_success_successReturned() throws Exception {
        mockLoginSuccess();
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        Assert.assertThat(result, CoreMatchers.is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    // Server error : Server error returned
    @Test
    public void updateUsername_serverError_failureReturned() throws Exception {
        mockServerError();
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        Assert.assertThat(result, CoreMatchers.is(UseCaseResult.FAILURE));
    }

    // Auth error : Auth error returned
    @Test
    public void updateUsername_authError_failureReturned() throws Exception {
        mockAuthError();
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        Assert.assertThat(result, CoreMatchers.is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_generalError_failureReturned() throws Exception {
        mockGeneralError();
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        Assert.assertThat(result, CoreMatchers.is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_networkError_networkErrorReturned() throws Exception {
        mockNetworkError();
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USER_NAME);
        Assert.assertThat(result, CoreMatchers.is(UseCaseResult.NETWORK_ERROR));
    }

    private void mockLoginSuccess() throws NetworkErrorException {
        when(usernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult
                        (EndpointResultStatus.SUCCESS, USER_ID, USER_NAME));
    }

    private void mockNetworkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(usernameHttpEndpointSyncMock).updateUsername(anyString(), anyString());
    }

    private void mockGeneralError() throws Exception {
        when(usernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(
                        EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void mockAuthError() throws Exception {
        when(usernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void mockServerError() throws Exception {
        when(usernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""));
    }

}