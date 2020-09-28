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

    public static final String USER_ID = "user_id";

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

    // 로그인 성공하면 UsersCache 에 캐슁된다.

    // 로그인 실패하면 UserCache 에 저장되지 않는다.

    // 서버 에러인 경우

    // 네트워크 에러인 경우

    // User profile 가져오기 성공하는 경우

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String userId;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;
            return null;
        }
    };

    private static class UsersCacheTd implements UsersCache {

        @Override
        public void cacheUser(User user) {

        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return null;
        }
    };
}