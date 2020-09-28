package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FetchUserProfileUseCaseSyncTest {

    private static final String EMPTY_USER_ID = "";
    public static final String USER_ID = "user_id";
    public static final String USER_FULL_NAME = "Jack Sparrow";
    public static final String PROFILE_IMG_URL = "http://www.maxst.com/img.png";

    // Please read description.txt content

    private FetchUserProfileUseCaseSync fetchUserProfileUseCaseSync;
    private UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    private UsersCacheTd usersCacheTd;

    @Before
    public void setup() {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();

        fetchUserProfileUseCaseSync = new FetchUserProfileUseCaseSync(
                userProfileHttpEndpointSyncTd,
                usersCacheTd);
    }

    // User id 가 end point 에 전달이 된다.
    @Test
    public void fetchProfile_success_userIdPassedToEndPoint() {
        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(userProfileHttpEndpointSyncTd.userId, is(USER_ID));
    }

    // 프로필 가져오기 성공하면 UsersCache 에 캐쉬된다.
    @Test
    public void fetchProfile_success_userCached() {
        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(usersCacheTd.userId, is(USER_ID));
        assertThat(usersCacheTd.fullName, is(USER_FULL_NAME));
        assertThat(usersCacheTd.imageUrl, is(PROFILE_IMG_URL));
    }

    // General 에러 발생하면 UserCache 에 저장되지 않는다.
    @Test
    public void fetchProfile_generalError_userNotCached() {
        userProfileHttpEndpointSyncTd.isGeneralError = true;
        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(usersCacheTd.userId, is(USER_ID));
    }

    // Network 에러인 경우 UserCache 에 저장되지 않는다.
    @Test
    public void fetchProfile_networkError_userNotCached() {
        userProfileHttpEndpointSyncTd.isNetworkError = true;
        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(usersCacheTd.userId, is(EMPTY_USER_ID));
    }

    // 서버 에러인 경우
    @Test
    public void fetchProfile_serverError_userNotCached() {
        userProfileHttpEndpointSyncTd.isServerError = true;
        fetchUserProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(usersCacheTd.userId, is(USER_ID));
    }

    // User profile 가져오기 성공하는 경우

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String userId;
        public boolean isGeneralError;
        public boolean isNetworkError;
        private boolean isServerError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;

            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, userId, "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, userId, "", "");
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, userId, USER_FULL_NAME, PROFILE_IMG_URL);
            }
        }
    };

    private static class UsersCacheTd implements UsersCache {

        public String userId = EMPTY_USER_ID;
        public String fullName;
        public String imageUrl;

        @Override
        public void cacheUser(User user) {
            this.userId = user.getUserId();
            this.fullName = user.getFullName();
            this.imageUrl = user.getImageUrl();
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return null;
        }
    };
}