package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WithdrawResult;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Bug Condition Exploration Test — 钱包提现 bug 条件探索测试
 *
 * <p><b>Validates: Requirements 2.1, 2.2, 2.7, 2.10</b></p>
 *
 * <p>This test encodes the EXPECTED (correct) behavior after the fix.
 * It is expected to FAIL on the current unfixed code, confirming the bug exists.</p>
 *
 * <p>Bug Condition: 业务失败返回 code=200 且无结构化错误</p>
 *
 * <p>Counterexamples documented:
 * <ul>
 *   <li>{@code applyWithdraw} returns {@code String}, not a structured object</li>
 *   <li>Controller wraps String as {@code AjaxResult.success()} when msg starts with "微信提现"</li>
 *   <li>No {@code WithdrawFailType} enum exists — no structured error classification</li>
 *   <li>No mutex check for pending withdrawals — concurrent requests not rejected</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Bug Exploration: 提现业务失败应返回非200 code + 结构化 failType")
class WalletWithdrawBugExplorationTest {

    @Mock
    private WalletMapper walletMapper;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IWalletTransferGateway walletTransferGateway;

    @InjectMocks
    private WalletServiceImpl walletService;

    private UserInfo validUserInfo;
    private UserWallet validWallet;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(walletService, "transferMinAmount", new BigDecimal("1.00"));
        ReflectionTestUtils.setField(walletService, "transferMaxAmount", new BigDecimal("5000.00"));
        ReflectionTestUtils.setField(walletService, "transferNotifyUrl", "https://zhiyujia.xyz/api/wxmini/pay/wallet/notify");
        ReflectionTestUtils.setField(walletService, "transferSceneId", "1005");
        ReflectionTestUtils.setField(walletService, "transferBatchName", "钱包提现");

        validUserInfo = new UserInfo();
        validUserInfo.setId(10L);
        validUserInfo.setIsRealnameAuth(1);
        validUserInfo.setRealName("张三");
        validUserInfo.setIdCard("110101199001010011");
        validUserInfo.setOpenId("openid-test-1");

        validWallet = new UserWallet();
        validWallet.setUid(10L);
        validWallet.setBalance(new BigDecimal("100.00"));
        validWallet.setFrozen(BigDecimal.ZERO);
        validWallet.setTotalEarned(new BigDecimal("100.00"));
        validWallet.setTotalWithdrawn(BigDecimal.ZERO);
    }

    /**
     * Test Case 1: SDK exception → should return structured failure with failType
     *
     * <p>When {@code walletTransferGateway.createTransfer} throws an exception,
     * the fixed code returns a WithdrawResult with success=false and a failType.</p>
     *
     * <p>Expected behavior: response code != 200 AND data.failType != null</p>
     *
     * <p><b>Validates: Requirements 2.1, 2.10</b></p>
     */
    @Test
    @DisplayName("SDK异常时应返回 code!=200 且包含 failType")
    void sdkExceptionShouldReturnNon200WithFailType() throws Exception {
        // Arrange: valid user, valid wallet, but SDK throws exception
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(validUserInfo);
        when(walletMapper.selectWalletByUid(10L)).thenReturn(validWallet);
        when(walletMapper.selectPendingWithdrawCountByUid(10L)).thenReturn(0);
        when(walletMapper.insertWithdraw(any())).thenAnswer(invocation -> {
            WalletWithdraw w = invocation.getArgument(0);
            w.setId(1L);
            return 1;
        });
        when(walletTransferGateway.createTransfer(any())).thenThrow(new RuntimeException("SYSTEMERROR"));

        // Act
        WithdrawResult result = walletService.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        // Simulate controller logic
        AjaxResult ajaxResult;
        if (result.isSuccess()) {
            ajaxResult = AjaxResult.success(result.getMsg(), result);
        } else {
            ajaxResult = AjaxResult.error(result.getUserMessage(), result);
        }

        int code = (int) ajaxResult.get(AjaxResult.CODE_TAG);
        Object data = ajaxResult.get(AjaxResult.DATA_TAG);

        // Assert: Response code should NOT be 200 for a failed withdraw
        assertNotEquals(200, code,
                "SDK exception should cause non-200 response code");

        // Assert: Response should contain structured failType
        assertNotNull(data, "Response should contain structured data with failType");
        assertTrue(data instanceof WithdrawResult, "Response data should be a WithdrawResult");
        WithdrawResult resultData = (WithdrawResult) data;
        assertNotNull(resultData.getFailType(),
                "Response should contain a failType field for error classification");
        assertFalse(resultData.isSuccess(), "Result should indicate failure");
    }

    /**
     * Test Case 2: OpenID missing → should return code!=200 with failType="OPENID_MISSING"
     *
     * <p>When {@code userInfo.openId} is null/blank, the fixed code returns a structured
     * WithdrawResult with failType=OPENID_MISSING.</p>
     *
     * <p>Expected behavior: code != 200 AND data.failType = "OPENID_MISSING"</p>
     *
     * <p><b>Validates: Requirements 2.2</b></p>
     */
    @Test
    @DisplayName("openId缺失时应返回 failType=OPENID_MISSING")
    void openIdMissingShouldReturnFailTypeOpenIdMissing() {
        // Arrange: user with null openId
        UserInfo userInfoNoOpenId = new UserInfo();
        userInfoNoOpenId.setId(10L);
        userInfoNoOpenId.setIsRealnameAuth(1);
        userInfoNoOpenId.setRealName("张三");
        userInfoNoOpenId.setIdCard("110101199001010011");
        userInfoNoOpenId.setOpenId(null); // Missing openId

        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfoNoOpenId);

        // Act
        WithdrawResult result = walletService.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        // Simulate controller logic
        AjaxResult ajaxResult;
        if (result.isSuccess()) {
            ajaxResult = AjaxResult.success(result.getMsg(), result);
        } else {
            ajaxResult = AjaxResult.error(result.getUserMessage(), result);
        }

        int code = (int) ajaxResult.get(AjaxResult.CODE_TAG);
        Object data = ajaxResult.get(AjaxResult.DATA_TAG);

        // Assert code is not 200
        assertNotEquals(200, code, "OpenID missing should return non-200 code");

        // Assert structured failType
        assertNotNull(data, "Response should contain structured data");
        assertTrue(data instanceof WithdrawResult, "Response data should be a WithdrawResult");
        WithdrawResult resultData = (WithdrawResult) data;
        assertEquals("OPENID_MISSING", resultData.getFailType(),
                "Should return failType=OPENID_MISSING when openId is missing");

        // Should NOT have created a withdraw record (pre-validation failure)
        verify(walletMapper, never()).insertWithdraw(any(WalletWithdraw.class));
    }

    /**
     * Test Case 3: Concurrent pending withdraw → should reject second request
     *
     * <p>When the same uid already has a withdraw with status=0 (PROCESSING),
     * the fixed code rejects the new request with PENDING_WITHDRAW_EXISTS.</p>
     *
     * <p>Expected behavior: code != 200 AND failType = "PENDING_WITHDRAW_EXISTS"</p>
     *
     * <p><b>Validates: Requirements 2.7</b></p>
     */
    @Test
    @DisplayName("同用户存在处理中提现时应拒绝新请求")
    void concurrentPendingWithdrawShouldBeRejected() throws Exception {
        // Arrange: valid user with existing PROCESSING withdraw
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(validUserInfo);
        when(walletMapper.selectWalletByUid(10L)).thenReturn(validWallet);
        // Mutex check returns 1 (existing pending withdraw)
        when(walletMapper.selectPendingWithdrawCountByUid(10L)).thenReturn(1);

        // Act
        WithdrawResult result = walletService.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        // Simulate controller logic
        AjaxResult ajaxResult;
        if (result.isSuccess()) {
            ajaxResult = AjaxResult.success(result.getMsg(), result);
        } else {
            ajaxResult = AjaxResult.error(result.getUserMessage(), result);
        }

        int code = (int) ajaxResult.get(AjaxResult.CODE_TAG);
        Object data = ajaxResult.get(AjaxResult.DATA_TAG);

        // Assert: The request should be rejected
        assertNotEquals(200, code,
                "Concurrent pending withdraw should be rejected with non-200 code");

        // Should have structured failType
        assertNotNull(data, "Response should contain structured rejection data");
        assertTrue(data instanceof WithdrawResult, "Response data should be a WithdrawResult");
        WithdrawResult resultData = (WithdrawResult) data;
        assertEquals("PENDING_WITHDRAW_EXISTS", resultData.getFailType(),
                "Should return failType=PENDING_WITHDRAW_EXISTS for concurrent withdraw");

        // Should NOT have created a new withdraw record
        verify(walletMapper, never()).insertWithdraw(any(WalletWithdraw.class));
    }
}
