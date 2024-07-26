package com.caiwei.online_judge.mapper;

import com.caiwei.online_judge.model.entity.PersonalQuestion;
import com.caiwei.online_judge.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caiwei.online_judge.model.vo.PersonalQuestionVO;

import java.util.List;

/**
* @author caiwei
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-05-29 22:55:30
* @Entity com.caiwei.online_judge.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    void updateSubmitNum(Long questionId);
    void updateAcceptNum(Long questionId);

    List<PersonalQuestion> getPersonalQuestion(Long userId);
}




