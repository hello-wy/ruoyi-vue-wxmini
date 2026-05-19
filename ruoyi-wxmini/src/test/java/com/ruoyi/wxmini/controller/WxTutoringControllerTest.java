package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import com.ruoyi.wxmini.service.IBabyInfoService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IUserServiceAddressService;
import com.ruoyi.wxmini.service.IWxRealVerifyService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxTutoringControllerTest {

    private static final String USER_ID = "123";
    private static final String WECHAT_USER_ID = "6723e06a-5f1d-4479-9b59-b8df275dea40";

    @Mock
    private ITutorsService tutorsService;
    @Mock
    private IParentsService parentsService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IBabyInfoService babyInfoService;
    @Mock
    private IUserServiceAddressService userServiceAddressService;
    @Mock
    private IWxRealVerifyService wxRealVerifyService;

    @InjectMocks
    private WxTutoringController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void listTutorsShouldBeAnonymous() throws Exception {
        Method method = WxTutoringController.class.getMethod(
                "listTutors",
                long.class,
                long.class,
                String.class,
                String.class,
                Long.class,
                Long.class
        );

        assertNotNull(method.getAnnotation(Anonymous.class));
    }

    @Test
    void myTutorShouldReturn200WhenTutorMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void myTutorShouldReturn200WhenTutorFound() {
        Tutors tutor = new Tutors();
        tutor.setUid(USER_ID);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(tutor);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void applyTutorShouldPersistRealnameToUserInfoInsteadOfTutors() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setSchool("南大");
        request.setIdentity(1L);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.insertTutors(any(Tutors.class))).thenAnswer(invocation -> {
            Tutors tutor = invocation.getArgument(0);
            tutor.setId(1001L);
            return 1;
        });

        AjaxResult result = controller.applyTutor(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("申请成功，请等待审核", result.get(AjaxResult.MSG_TAG));
        assertEquals(1001L, result.get(AjaxResult.DATA_TAG));
        verify(userInfoService).updateRealnameInfo(USER_ID, "张三", "110105199001011234");
        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).insertTutors(captor.capture());
        Tutors saved = captor.getValue();
        assertEquals(USER_ID, saved.getUid());
        assertEquals(0L, saved.getStatus());
        assertEquals(1001L, saved.getId());
        assertNull(saved.getRealName());
        assertNull(saved.getIdCard());
        assertEquals("南大", saved.getSchool());
    }

    @Test
    void applyTutorShouldRejectUniversityTutorWithoutCurrentGrade() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(0L);
        request.setSchool("南京大学");
        request.setMajor("数学");
        request.setDegree(1L);

        AjaxResult result = controller.applyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择当前年级", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).insertTutors(any(Tutors.class));
    }

    @Test
    void applyTutorShouldClearCurrentGradeForNonUniversityTutor() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(1L);
        request.setCurrentGrade("大三");
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.insertTutors(any(Tutors.class))).thenReturn(1);

        controller.applyTutor(request);

        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).insertTutors(captor.capture());
        assertNull(captor.getValue().getCurrentGrade());
    }

    @Test
    void applyTutorShouldReturnErrorWhenUserInfoMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(0);

        AjaxResult result = controller.applyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("用户不存在", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).insertTutors(any(Tutors.class));
    }

    @Test
    void updateMyTutorShouldResetStatusToPendingReview() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors existing = new Tutors();
        existing.setId(1001L);
        existing.setUid(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setSchool("南京大学");
        request.setIdentity(1L);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(existing);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.updateTutors(any(Tutors.class))).thenReturn(1);

        AjaxResult result = controller.updateMyTutor(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("资料已更新，请等待审核", result.get(AjaxResult.MSG_TAG));
        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).updateTutors(captor.capture());
        Tutors saved = captor.getValue();
        assertEquals(1001L, saved.getId());
        assertEquals(USER_ID, saved.getUid());
        assertEquals(0L, saved.getStatus());
        assertNull(saved.getRealName());
        assertNull(saved.getIdCard());
        assertEquals("南京大学", saved.getSchool());
    }

    @Test
    void updateMyTutorShouldRejectUniversityTutorWithoutCurrentGrade() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors existing = new Tutors();
        existing.setId(1001L);
        existing.setUid(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(0L);

        AjaxResult result = controller.updateMyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择当前年级", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).updateTutors(any(Tutors.class));
    }

    @Test
    void getParentDetailShouldReturnParentWhenFound() {
        Parents parent = new Parents();
        parent.setId(9L);
        when(parentsService.selectParentsById(9L)).thenReturn(parent);

        AjaxResult result = controller.getParentDetail(9L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(parent, result.get(AjaxResult.DATA_TAG));
    }

    @Test
    void updateMyParentShouldRejectDemandOwnedByAnotherUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents existing = new Parents();
        existing.setId(9L);
        existing.setWechatUid("other-user");
        Parents request = buildValidParentRequest(7L, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsById(9L)).thenReturn(existing);

        AjaxResult result = controller.updateMyParent(9L, request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("无权操作该需求", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).updateParents(any(Parents.class));
    }

    @Test
    void updateMyParentShouldPersistOwnedDemand() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents existing = new Parents();
        existing.setId(9L);
        existing.setWechatUid(WECHAT_USER_ID);
        existing.setSystemUid(3L);
        existing.setStatus(1L);
        Parents request = buildValidParentRequest(7L, 11L);
        request.setName("数学辅导");
        request.setMethods(1L);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        UserServiceAddress address = buildAddress(11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsById(9L)).thenReturn(existing);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(userServiceAddressService.selectAddressByIdAndUserId(11L, WECHAT_USER_ID)).thenReturn(address);
        when(parentsService.updateParents(any(Parents.class))).thenReturn(1);

        AjaxResult result = controller.updateMyParent(9L, request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("操作成功", result.get(AjaxResult.MSG_TAG));
        ArgumentCaptor<Parents> captor = ArgumentCaptor.forClass(Parents.class);
        verify(parentsService).updateParents(captor.capture());
        Parents saved = captor.getValue();
        assertEquals(9L, saved.getId());
        assertEquals(WECHAT_USER_ID, saved.getWechatUid());
        assertEquals(3L, saved.getSystemUid());
        assertEquals(7L, saved.getBabyId());
        assertEquals("数学辅导", saved.getName());
        assertEquals(1L, saved.getMethods());
        assertEquals(11L, saved.getAddressId());
        assertEquals("南京市江宁区", saved.getRegion());
        assertEquals("百家湖 88号 1单元", saved.getLocation());
        assertEquals("120.1,31.9", saved.getGeo());
    }

    @Test
    void deleteMyParentShouldRejectDemandOwnedByAnotherUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        Parents existing = new Parents();
        existing.setId(9L);
        existing.setWechatUid("other-user");
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsById(9L)).thenReturn(existing);

        AjaxResult result = controller.deleteMyParent(9L);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("无权操作该需求", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).deleteParentsById(9L);
    }

    @Test
    void deleteMyParentShouldDeleteOwnedDemand() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        Parents existing = new Parents();
        existing.setId(9L);
        existing.setWechatUid(WECHAT_USER_ID);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsById(9L)).thenReturn(existing);
        when(parentsService.deleteParentsById(9L)).thenReturn(1);

        AjaxResult result = controller.deleteMyParent(9L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("删除成功", result.get(AjaxResult.MSG_TAG));
        verify(parentsService).deleteParentsById(9L);
    }

    @Test
    void addParentsShouldReturnErrorWhenBabyIdMissing() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents request = buildValidParentRequest(null, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择服务萌娃", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldReturnErrorWhenBabyDoesNotBelongToCurrentUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents request = buildValidParentRequest(99L, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(99L, WECHAT_USER_ID)).thenReturn(null);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("萌娃信息不存在或无权使用", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldReturn200ForWechatUserIdString() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        UserServiceAddress address = buildAddress(11L);
        Parents request = buildValidParentRequest(7L, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(userServiceAddressService.selectAddressByIdAndUserId(11L, WECHAT_USER_ID)).thenReturn(address);
        when(parentsService.selectSingleParentByWechatUid(WECHAT_USER_ID)).thenReturn(null);
        when(parentsService.insertParents(any(Parents.class))).thenReturn(0);

        AjaxResult result = controller.addParents(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<Parents> captor = ArgumentCaptor.forClass(Parents.class);
        verify(parentsService).insertParents(captor.capture());
        Parents saved = captor.getValue();
        assertEquals(WECHAT_USER_ID, saved.getWechatUid());
        assertEquals(7L, saved.getBabyId());
        assertEquals(11L, saved.getAddressId());
        assertEquals("南京市江宁区", saved.getRegion());
        assertEquals("百家湖 88号 1单元", saved.getLocation());
        assertEquals("120.1,31.9", saved.getGeo());
    }

    @Test
    void addParentsShouldNormalizeServiceSlotsAndBackfillLegacyFields() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        UserServiceAddress address = buildAddress(11L);
        Parents request = buildValidParentRequest(7L, 11L);
        request.setServiceTimes("[{\"serviceDate\":\"2026-05-03\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"},{\"serviceDate\":\"2026-05-01\",\"startTime\":\"09:30\",\"endTime\":\"11:30\"}]");
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(userServiceAddressService.selectAddressByIdAndUserId(11L, WECHAT_USER_ID)).thenReturn(address);
        when(parentsService.selectSingleParentByWechatUid(WECHAT_USER_ID)).thenReturn(null);

        controller.addParents(request);

        ArgumentCaptor<Parents> captor = ArgumentCaptor.forClass(Parents.class);
        verify(parentsService).insertParents(captor.capture());
        Parents saved = captor.getValue();
        assertEquals("2026-05-03,2026-05-01", saved.getServiceDates());
        assertEquals("7,5", saved.getDayOfWeek());
        assertEquals(LocalTime.parse("18:00"), saved.getStartTime());
        assertEquals(LocalTime.parse("20:00"), saved.getEndTime());
        assertEquals("[{\"serviceDate\":\"2026-05-03\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"},{\"serviceDate\":\"2026-05-01\",\"startTime\":\"09:30\",\"endTime\":\"11:30\"}]", saved.getServiceTimes());
    }

    @Test
    void addParentsShouldRejectMalformedServiceSchedulePayload() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        Parents request = buildValidParentRequest(7L, 11L);
        request.setServiceTimes("not-json");
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(userServiceAddressService.selectAddressByIdAndUserId(11L, WECHAT_USER_ID)).thenReturn(buildAddress(11L));
        when(parentsService.selectSingleParentByWechatUid(WECHAT_USER_ID)).thenReturn(null);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请完善服务时段", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldRejectDuplicateDemandForSameWechatUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents existing = new Parents();
        existing.setId(77L);
        Parents request = buildValidParentRequest(7L, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectSingleParentByWechatUid(WECHAT_USER_ID)).thenReturn(existing);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("你已发布需求，请前往详情编辑", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldRejectNonParentUserBeforeBindingOrAddressChecks() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(1);
        Parents request = buildValidParentRequest(7L, 11L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请先切换为家长身份", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
        verify(babyInfoService, never()).selectBabyInfoByIdAndUserId(any(), any());
        verify(userServiceAddressService, never()).selectAddressByIdAndUserId(any(), any());
    }

    @Test
    void updateMyParentShouldRejectAddressOwnedByAnotherUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents existing = new Parents();
        existing.setId(9L);
        existing.setWechatUid(WECHAT_USER_ID);
        existing.setSystemUid(3L);
        Parents request = buildValidParentRequest(7L, 11L);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsById(9L)).thenReturn(existing);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(userServiceAddressService.selectAddressByIdAndUserId(11L, WECHAT_USER_ID)).thenReturn(null);

        AjaxResult result = controller.updateMyParent(9L, request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("服务地址不存在或无权使用", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).updateParents(any(Parents.class));
    }

    @Test
    void realVerifyShouldReturnVerifyResult() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        WxRealVerifyRequestBo body = new WxRealVerifyRequestBo();
        body.setRealName("张三");
        body.setIdCard("110105199001011234");
        WxRealVerifyResultVo payload = new WxRealVerifyResultVo();
        payload.setMatched(Boolean.TRUE);
        when(wxRealVerifyService.verify(USER_ID, body)).thenReturn(payload);

        AjaxResult result = controller.verifyRealName(body);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(payload, result.get(AjaxResult.DATA_TAG));
    }

    private Parents buildValidParentRequest(Long babyId, Long addressId) {
        Parents request = new Parents();
        request.setBabyId(babyId);
        request.setAddressId(addressId);
        request.setName("数学辅导");
        request.setPhone("13800138000");
        request.setGrade("三年级");
        request.setSubject("数学");
        request.setServiceTimes("[{\"serviceDate\":\"2026-05-01\",\"startTime\":\"09:00\",\"endTime\":\"11:00\"}]");
        request.setMethods(1L);
        request.setDemandItems("作业辅导");
        request.setGenderRequirement(0);
        request.setHourlyBudget(new BigDecimal("150"));
        return request;
    }

    private UserServiceAddress buildAddress(Long id) {
        UserServiceAddress address = new UserServiceAddress();
        address.setId(id);
        address.setRegion("南京市江宁区");
        address.setLocation("百家湖");
        address.setAddressDetail("88号");
        address.setDoorplate("1单元");
        address.setGeo("120.1,31.9");
        return address;
    }
}
