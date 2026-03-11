package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Questionnaire;

/**
 * 问卷调查配置Service接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface IQuestionnaireService 
{
    /**
     * 查询问卷调查配置
     * 
     * @param id 问卷调查配置主键
     * @return 问卷调查配置
     */
    public Questionnaire selectQuestionnaireById(Long id);

    /**
     * 查询问卷调查配置列表
     * 
     * @param questionnaire 问卷调查配置
     * @return 问卷调查配置集合
     */
    public List<Questionnaire> selectQuestionnaireList(Questionnaire questionnaire);

    /**
     * 新增问卷调查配置
     * 
     * @param questionnaire 问卷调查配置
     * @return 结果
     */
    public int insertQuestionnaire(Questionnaire questionnaire);

    /**
     * 修改问卷调查配置
     * 
     * @param questionnaire 问卷调查配置
     * @return 结果
     */
    public int updateQuestionnaire(Questionnaire questionnaire);

    /**
     * 批量删除问卷调查配置
     * 
     * @param ids 需要删除的问卷调查配置主键集合
     * @return 结果
     */
    public int deleteQuestionnaireByIds(Long[] ids);

    /**
     * 删除问卷调查配置信息
     * 
     * @param id 问卷调查配置主键
     * @return 结果
     */
    public int deleteQuestionnaireById(Long id);


    public List<Questionnaire> selectRecentQuestionnaireList(Long lectureId);
}
