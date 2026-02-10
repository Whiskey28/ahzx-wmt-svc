package com.wmt.module.ai.service.content;

import com.wmt.module.ai.controller.admin.content.vo.*;

import java.util.List;

/**
 * 内容自动化 Service 接口
 * 用于 n8n 工作流调用，实现从 mediaCrawler 数据到内容生成和发布的自动化流程
 *
 * @author Auto Generated
 */
public interface ContentAutomationService {

    /**
     * 查询 mediaCrawler 数据
     *
     * @param queryReqVO 查询请求
     * @return 数据列表
     */
    List<CrawlerDataRespVO> queryCrawlerData(CrawlerDataQueryReqVO queryReqVO);

    /**
     * 生成文案
     *
     * @param generateReqVO 生成请求
     * @return 生成的文案
     */
    ContentGenerateRespVO generateText(ContentTextGenerateReqVO generateReqVO);

    /**
     * 生成图片
     *
     * @param generateReqVO 生成请求
     * @return 生成的图片
     */
    ContentImageGenerateRespVO generateImage(ContentImageGenerateReqVO generateReqVO);

    /**
     * 生成完整内容（文案+图片）
     *
     * @param generateReqVO 生成请求
     * @return 生成的完整内容
     */
    ContentFullGenerateRespVO generateFullContent(ContentFullGenerateReqVO generateReqVO);

    /**
     * 发布到小红书
     *
     * @param publishReqVO 发布请求
     * @return 发布结果
     */
    ContentPublishRespVO publishToXiaohongshu(ContentPublishReqVO publishReqVO);

    /**
     * 发布到抖音
     *
     * @param publishReqVO 发布请求
     * @return 发布结果
     */
    ContentPublishRespVO publishToDouyin(ContentPublishReqVO publishReqVO);

    /**
     * 更新数据状态
     *
     * @param updateReqVO 更新请求
     */
    void updateDataStatus(ContentStatusUpdateReqVO updateReqVO);

    /**
     * 检查敏感词
     *
     * @param content 待检查的内容
     * @return 检查结果
     */
    ContentSensitivityCheckRespVO checkSensitivity(String content);
}
