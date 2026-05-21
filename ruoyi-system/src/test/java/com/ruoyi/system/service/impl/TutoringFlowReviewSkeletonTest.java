package com.ruoyi.system.service.impl;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Review-track executable skeletons for the tutoring closed-loop refactor.
 *
 * <p>These tests intentionally skip until the target classes land in the backend worktree.
 * Once implementation exists, turn each skeleton into concrete Mockito-based tests first,
 * then wire them into the final acceptance run.</p>
 */
class TutoringFlowReviewSkeletonTest {

    @Test
    void tutoringScheduleServiceShouldExposeLifecycleEntryPoints() throws Exception {
        Class<?> clazz = loadIfPresent("com.ruoyi.system.service.impl.TutoringScheduleServiceImpl");
        assumeTrue(clazz != null, "待主实现落地 TutoringScheduleServiceImpl 后启用");

        assertHasMethodContaining(clazz, "create");
        assertHasMethodContaining(clazz, "cancel");
        assertHasMethodContaining(clazz, "complete");
        assertHasMethodContaining(clazz, "pay");
    }

    @Test
    void tutoringScheduleStateMachineShouldRejectBackwardTransition() throws Exception {
        Class<?> clazz = loadIfPresent("com.ruoyi.system.service.impl.TutoringScheduleServiceImpl");
        assumeTrue(clazz != null, "待主实现落地 TutoringScheduleServiceImpl 后补充状态流转断言");

        assertHasMethodContaining(clazz, "updateStatus");
        // Concrete assertions to add when implementation lands:
        // 1. CREATED/WAITING_PAY -> PAID -> SCHEDULED -> COMPLETED is allowed.
        // 2. COMPLETED cannot transition back to SCHEDULED/PAID.
        // 3. CANCELED is terminal and cannot generate payroll items.
    }

    @Test
    void tutoringPayrollServiceShouldProtectWalletCreditIdempotency() throws Exception {
        Class<?> clazz = loadIfPresent("com.ruoyi.system.service.impl.TutoringPayrollServiceImpl");
        assumeTrue(clazz != null, "待主实现落地 TutoringPayrollServiceImpl 后启用");

        assertHasMethodContaining(clazz, "pay");
        assertHasMethodContaining(clazz, "settle");
        // Concrete assertions to add when implementation lands:
        // 1. Same payroll item / same callback requestNo executed twice -> walletService.creditPayroll invoked once.
        // 2. wallet_transaction contains exactly one INCOME row for the same tutoring payroll bizId.
        // 3. Duplicate callback must be treated as success/no-op, not an exception that triggers retry storms.
    }

    @Test
    void tutoringBindingServiceShouldValidateRoleAndOwnershipBeforeInsert() throws Exception {
        Class<?> clazz = loadIfPresent("com.ruoyi.system.service.impl.TutoringBindingServiceImpl");
        assumeTrue(clazz != null, "待主实现落地 TutoringBindingServiceImpl 后启用");

        assertHasMethodContaining(clazz, "bind");
        // Concrete assertions to add when implementation lands:
        // 1. Parent side uses wechatUid as primary ownership key, not systemUid-only matching.
        // 2. Binding rejects tutor/parent self-binding and duplicate active binding.
        // 3. Binding creation must verify tutoring order still belongs to current parent and is in bindable status.
    }

    @Test
    void tutoringOrderServiceShouldValidateSingleActiveOrderPerBindingAndSchedulePayload() throws Exception {
        Class<?> clazz = loadIfPresent("com.ruoyi.system.service.impl.TutoringOrderServiceImpl");
        assumeTrue(clazz != null, "待主实现落地 TutoringOrderServiceImpl 后启用");

        assertHasMethodContaining(clazz, "create");
        assertHasMethodContaining(clazz, "update");
        // Concrete assertions to add when implementation lands:
        // 1. One active tutoring order per binding unless previous order is canceled/completed.
        // 2. Empty or malformed schedule payload is rejected before persistence.
        // 3. Order amount source (hourlyBudget * slotHours or agreed total) is explicit and documented.
    }

    private static Class<?> loadIfPresent(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static void assertHasMethodContaining(Class<?> clazz, String token) {
        Method[] methods = clazz.getDeclaredMethods();
        assertTrue(Arrays.stream(methods)
                        .map(Method::getName)
                        .anyMatch(name -> name.toLowerCase().contains(token.toLowerCase())),
                () -> clazz.getSimpleName() + " should expose a method containing '" + token + "'");
    }
}
