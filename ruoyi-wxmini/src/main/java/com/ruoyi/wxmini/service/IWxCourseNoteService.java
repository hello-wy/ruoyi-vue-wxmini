package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.WxCourseNoteSaveBo;
import com.ruoyi.wxmini.bo.WxCourseNoteUpdateBo;
import com.ruoyi.wxmini.vo.WxCourseNoteVo;

import java.util.List;

public interface IWxCourseNoteService {
    List<WxCourseNoteVo> listMyNotes(String userId);

    WxCourseNoteVo createMyNote(String userId, WxCourseNoteSaveBo bo);

    WxCourseNoteVo updateMyNote(String userId, Long noteId, WxCourseNoteUpdateBo bo);

    void deleteMyNote(String userId, Long noteId);
}
