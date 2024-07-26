package com.caiwei.online_judge.judge;

import cn.hutool.json.JSONUtil;
import com.caiwei.online_judge.common.ErrorCode;
import com.caiwei.online_judge.exception.BusinessException;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBox;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBoxFactory;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBoxProxy;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeResponse;
import com.caiwei.online_judge.judge.strategy.JudgeManager;
import com.caiwei.online_judge.judge.strategy.context.JudgeContext;
import com.caiwei.online_judge.model.dto.question.JudgeCase;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;
import com.caiwei.online_judge.model.entity.Question;
import com.caiwei.online_judge.model.entity.QuestionSubmit;
import com.caiwei.online_judge.model.enums.JudgeInfoMessageEnum;
import com.caiwei.online_judge.model.enums.QuestionSubmitStatusEnum;
import com.caiwei.online_judge.service.QuestionService;
import com.caiwei.online_judge.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeManager judgeManager;
    //调用哪种类型的代码沙箱
    @Value("${codeSandBox.type}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1.获得提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);

        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);

        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        //更新提交数，使其+1
        questionService.updateSubmitNum(questionId);
        //不为等待状态就返回报错
        if (!(questionSubmit.getStatus()).equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中");
        }

        //2.更新题目提交的状态，为判题中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean updateStatus = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateStatus) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交信息更新错误");
        }

        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> JudgeCaselist = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = JudgeCaselist.stream().map(JudgeCase::getInput).collect(Collectors.toList());


        //3.调用代码沙箱
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest().builder()
                .inputList(inputList)
                .language(language)
                .code(code).build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        //4.获得沙箱执行后的输出信息
        List<String> outputList = executeCodeResponse.getOutputList();
        String message = executeCodeResponse.getMessage();
        String status = executeCodeResponse.getStatus();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setQuestion(question);
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setJudgeCaseList(JudgeCaselist);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestionSubmit(questionSubmit);

        //5.执行策略
        JudgeInfo judgeInfoResponse = judgeManager.doJudge(judgeContext);

        //如果执行后判题显示成功，更新通过数+1
        String judgeMessage = judgeInfoResponse.getMessage();
        if (judgeMessage.equals(JudgeInfoMessageEnum.ACCEPT.getValue())) {
            //题目正确
            questionService.updateAcceptNum(questionId);
        }
        //6.判题结束，更新题目提交的状态，成功
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResponse));
        updateStatus = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateStatus) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交信息更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}
