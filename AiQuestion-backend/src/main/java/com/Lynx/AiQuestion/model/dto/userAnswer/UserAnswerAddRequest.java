package com.Lynx.AiQuestion.model.dto.userAnswer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建用户答案请求
 */
@Data
public class UserAnswerAddRequest implements Serializable {

    /**
     *  id(用户答案id，为了保证用户答案的幂等性)
     */
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    private static final long serialVersionUID = 1L;
}