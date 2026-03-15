package com.ruoyi.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.QuestionnaireMapper;
import com.ruoyi.system.domain.Questionnaire;
import com.ruoyi.system.service.IQuestionnaireService;

/**
 * 问卷调查配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService 
{
    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    /**
     * 查询问卷调查配置
     * 
     * @param id 问卷调查配置主键
     * @return 问卷调查配置
     */
    @Override
    public Questionnaire selectQuestionnaireById(Long id)
    {
        return questionnaireMapper.selectQuestionnaireById(id);
    }

    /**
     * 查询问卷调查配置列表
     * 
     * @param questionnaire 问卷调查配置
     * @return 问卷调查配置
     */
    @Override
    public List<Questionnaire> selectQuestionnaireList(Questionnaire questionnaire)
    {
        return questionnaireMapper.selectQuestionnaireList(questionnaire);
    }

    /**
     * 新增问卷调查配置
     * 
     * @param questionnaire 问卷调查配置
     * @return 结果
     */
    @Override
    public int insertQuestionnaire(Questionnaire questionnaire)
    {
        questionnaire.setCreateTime(DateUtils.getNowDate());
        return questionnaireMapper.insertQuestionnaire(questionnaire);
    }

    /**
     * 修改问卷调查配置
     * 
     * @param questionnaire 问卷调查配置
     * @return 结果
     */
    @Override
    public int updateQuestionnaire(Questionnaire questionnaire)
    {
        questionnaire.setUpdateTime(DateUtils.getNowDate());
        return questionnaireMapper.updateQuestionnaire(questionnaire);
    }

    /**
     * 批量删除问卷调查配置
     * 
     * @param ids 需要删除的问卷调查配置主键
     * @return 结果
     */
    @Override
    public int deleteQuestionnaireByIds(Long[] ids)
    {
        return questionnaireMapper.deleteQuestionnaireByIds(ids);
    }

    /**
     * 删除问卷调查配置信息
     * 
     * @param id 问卷调查配置主键
     * @return 结果
     */
    @Override
    public int deleteQuestionnaireById(Long id)
    {
        return questionnaireMapper.deleteQuestionnaireById(id);
    }

    @Override
    public List<Questionnaire> selectRecentQuestionnaireList(Long lectureId) {
        LambdaQueryWrapper<Questionnaire> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Questionnaire::getLectureId, lectureId)
                .eq(Questionnaire::getStatus, 1);

        return questionnaireMapper.selectList(wrapper);
    }
}
