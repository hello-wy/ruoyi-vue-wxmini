package com.ruoyi.wxmini.service.impl;

import com.github.pagehelper.Page;
import com.ruoyi.wxmini.domain.vo.ReferralTreePageVo;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import com.ruoyi.wxmini.mapper.UserReferralMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserReferralServiceImplTest {

    @Test
    void getMyReferralTreeShouldGroupCurrentPageChildrenByLevelOneInvitee() {
        UserReferralVo inviteeA = referral("user-a", "root-user");
        UserReferralVo inviteeB = referral("user-b", "root-user");
        Page<UserReferralVo> currentPage = new Page<>(1, 1);
        currentPage.add(inviteeA);
        currentPage.add(inviteeB);
        currentPage.setTotal(7L);
        TestMapper mapper = new TestMapper(currentPage, Arrays.asList(
                referral("user-a-child", "user-a"), referral("user-b-child", "user-b")));

        ReferralTreePageVo result = service(mapper.proxy()).getMyReferralTree("root-user");

        assertEquals(7L, result.getTotal());
        assertEquals(2, result.getRows().size());
        assertEquals("user-a-child", result.getRows().get(0).getLevel2Invitees().get(0).getUserId());
        assertEquals("user-b-child", result.getRows().get(1).getLevel2Invitees().get(0).getUserId());
        assertEquals(Arrays.asList("user-a", "user-b"), mapper.batchInviterUserIds);
    }

    @Test
    void getMyReferralTreeShouldNotQueryChildrenWhenCurrentPageIsEmpty() {
        TestMapper mapper = new TestMapper(Collections.emptyList(), Collections.emptyList());

        ReferralTreePageVo result = service(mapper.proxy()).getMyReferralTree("root-user");

        assertEquals(0L, result.getTotal());
        assertEquals(0, result.getRows().size());
        assertNull(mapper.batchInviterUserIds);
    }

    private UserReferralServiceImpl service(UserReferralMapper mapper) {
        UserReferralServiceImpl service = new UserReferralServiceImpl();
        try {
            Field field = UserReferralServiceImpl.class.getDeclaredField("userReferralMapper");
            field.setAccessible(true);
            field.set(service, mapper);
            return service;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError(exception);
        }
    }

    private UserReferralVo referral(String userId, String inviterUserId) {
        UserReferralVo referral = new UserReferralVo();
        referral.setUserId(userId);
        referral.setInviterUserId(inviterUserId);
        return referral;
    }

    private static class TestMapper {
        private final List<UserReferralVo> level1Invitees;
        private final List<UserReferralVo> level2Invitees;
        private List<String> batchInviterUserIds;

        TestMapper(List<UserReferralVo> level1Invitees, List<UserReferralVo> level2Invitees) {
            this.level1Invitees = level1Invitees;
            this.level2Invitees = level2Invitees;
        }

        @SuppressWarnings("unchecked")
        UserReferralMapper proxy() {
            return (UserReferralMapper) Proxy.newProxyInstance(
                    getClass().getClassLoader(), new Class<?>[]{UserReferralMapper.class}, (proxy, method, args) -> {
                        if ("selectReferralTreeLevel1Invitees".equals(method.getName())) return level1Invitees;
                        if ("selectReferralVoListByInviterUserIds".equals(method.getName())) {
                            batchInviterUserIds = (List<String>) args[0];
                            return level2Invitees;
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
