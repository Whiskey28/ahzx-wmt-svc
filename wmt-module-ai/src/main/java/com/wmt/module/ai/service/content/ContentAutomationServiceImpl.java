package com.wmt.module.ai.service.content;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.ai.controller.admin.content.vo.*;
import com.wmt.module.ai.controller.admin.image.vo.AiImageDrawReqVO;
import com.wmt.module.ai.controller.admin.write.vo.AiWriteGenerateReqVO;
import com.wmt.module.ai.enums.write.AiWriteTypeEnum;
import com.wmt.module.ai.service.image.AiImageService;
import com.wmt.module.ai.service.write.AiWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容自动化 Service 实现类
 *
 * @author Auto Generated
 */
@Service
@Slf4j
public class ContentAutomationServiceImpl implements ContentAutomationService {

    @Resource
    private AiWriteService writeService;

    @Resource
    private AiImageService imageService;

    // TODO: 注入 mediaCrawler 数据库访问服务
    // @Resource
    // private MediaCrawlerDataService mediaCrawlerDataService;

    // TODO: 注入敏感词检测服务
    // @Resource
    // private SensitiveWordService sensitiveWordService;

    @Override
    public List<CrawlerDataRespVO> queryCrawlerData(CrawlerDataQueryReqVO queryReqVO) {
        log.info("[queryCrawlerData][查询 mediaCrawler 数据，参数：{}]", queryReqVO);

        // TODO: 实现从 mediaCrawler 数据库查询数据的逻辑
        // 这里需要根据实际的 mediaCrawler 数据库结构来实现
        // 示例代码：
        /*
        List<MediaCrawlerDataDO> dataList = mediaCrawlerDataService.queryData(
            queryReqVO.getKeywords(),
            queryReqVO.getPlatform(),
            queryReqVO.getProcessed(),
            queryReqVO.getLimit(),
            queryReqVO.getMinLikeCount(),
            queryReqVO.getMinCommentCount()
        );
        return convertList(dataList, this::convertToRespVO);
        */

        // 临时返回空列表，需要根据实际数据库结构实现
        return Collections.emptyList();
    }

    @Override
    public ContentGenerateRespVO generateText(ContentTextGenerateReqVO generateReqVO) {
        log.info("[generateText][生成文案，参数：{}]", generateReqVO);

        // 构建 AI 写作请求
        AiWriteGenerateReqVO writeReqVO = new AiWriteGenerateReqVO();
        writeReqVO.setType(AiWriteTypeEnum.ARTICLE.getType()); // 文章类型
        writeReqVO.setLength(generateReqVO.getLength() != null ? generateReqVO.getLength() : 500);
        writeReqVO.setFormat(1); // 格式：1-段落
        writeReqVO.setTone(1); // 语气：1-正式
        writeReqVO.setLanguage(1); // 语言：1-中文

        // 构建提示词
        String prompt = buildTextPrompt(generateReqVO);
        writeReqVO.setPrompt(prompt);

        // 如果有源数据，添加到原文
        if (StrUtil.isNotBlank(generateReqVO.getSourceData())) {
            Map<String, Object> sourceData = JSONUtil.toBean(generateReqVO.getSourceData(), Map.class);
            writeReqVO.setOriginalContent(JSONUtil.toJsonStr(sourceData));
        }

        // 调用 AI 写作服务（流式返回，需要收集完整内容）
        StringBuilder contentBuilder = new StringBuilder();
        Flux<com.wmt.framework.common.pojo.CommonResult<String>> flux = writeService.generateWriteContent(
            writeReqVO, 
            null // TODO: 获取当前用户 ID
        );

        // 收集流式返回的内容
        String generatedContent = flux
            .collectList()
            .block()
            .stream()
            .map(result -> result.getData())
            .filter(Objects::nonNull)
            .collect(Collectors.joining());

        // 解析生成的内容，提取标题和正文
        ContentGenerateRespVO respVO = new ContentGenerateRespVO();
        respVO.setContent(generatedContent);
        respVO.setTitle(extractTitle(generatedContent));
        respVO.setTags(extractTags(generatedContent, generateReqVO.getPlatform()));
        respVO.setGenerateTime(System.currentTimeMillis());

        return respVO;
    }

    @Override
    public ContentImageGenerateRespVO generateImage(ContentImageGenerateReqVO generateReqVO) {
        log.info("[generateImage][生成图片，参数：{}]", generateReqVO);

        // 构建 AI 图片生成请求
        AiImageDrawReqVO imageReqVO = new AiImageDrawReqVO();
        imageReqVO.setModelId(generateReqVO.getModelId());
        imageReqVO.setPrompt(generateReqVO.getPrompt());
        imageReqVO.setWidth(generateReqVO.getWidth());
        imageReqVO.setHeight(generateReqVO.getHeight());

        // 调用 AI 图片生成服务
        Long imageId = imageService.drawImage(null, imageReqVO); // TODO: 获取当前用户 ID

        // 等待图片生成完成（实际应该使用异步回调或轮询）
        // 这里简化处理，实际需要根据图片生成状态来获取结果
        ContentImageGenerateRespVO respVO = new ContentImageGenerateRespVO();
        respVO.setImageIds(Collections.singletonList(imageId));
        respVO.setImageUrls(Collections.emptyList()); // TODO: 从图片服务获取 URL
        respVO.setGenerateTime(System.currentTimeMillis());

        return respVO;
    }

    @Override
    public ContentFullGenerateRespVO generateFullContent(ContentFullGenerateReqVO generateReqVO) {
        log.info("[generateFullContent][生成完整内容，参数：{}]", generateReqVO);

        // 1. 生成文案
        ContentTextGenerateReqVO textReqVO = new ContentTextGenerateReqVO();
        textReqVO.setDataId(generateReqVO.getDataId());
        textReqVO.setSourceData(generateReqVO.getSourceData());
        textReqVO.setPlatform(generateReqVO.getPlatform());
        ContentGenerateRespVO textResult = generateText(textReqVO);

        // 2. 生成图片（如果需要）
        List<String> imageUrls = new ArrayList<>();
        if (Boolean.TRUE.equals(generateReqVO.getGenerateImage())) {
            ContentImageGenerateReqVO imageReqVO = generateReqVO.getImageConfig();
            if (imageReqVO == null) {
                imageReqVO = new ContentImageGenerateReqVO();
                imageReqVO.setPrompt(buildImagePrompt(textResult.getContent()));
                imageReqVO.setWidth(1024);
                imageReqVO.setHeight(1024);
            }
            ContentImageGenerateRespVO imageResult = generateImage(imageReqVO);
            imageUrls = imageResult.getImageUrls();
        }

        // 3. 组装结果
        ContentFullGenerateRespVO respVO = new ContentFullGenerateRespVO();
        respVO.setTitle(textResult.getTitle());
        respVO.setContent(textResult.getContent());
        respVO.setTags(textResult.getTags());
        respVO.setImageUrls(imageUrls);
        respVO.setGenerateTime(System.currentTimeMillis());

        return respVO;
    }

    @Override
    public ContentPublishRespVO publishToXiaohongshu(ContentPublishReqVO publishReqVO) {
        log.info("[publishToXiaohongshu][发布到小红书，参数：{}]", publishReqVO);

        // TODO: 实现小红书发布逻辑
        // 这里需要调用小红书开放平台 API 或使用第三方 SDK
        // 示例：
        /*
        try {
            XiaohongshuApiClient client = new XiaohongshuApiClient(token);
            PublishResult result = client.publish(
                publishReqVO.getTitle(),
                publishReqVO.getContent(),
                publishReqVO.getImages(),
                publishReqVO.getTags()
            );
            
            ContentPublishRespVO respVO = new ContentPublishRespVO();
            respVO.setSuccess(true);
            respVO.setPlatformPublishId(result.getPublishId());
            respVO.setPublishUrl(result.getUrl());
            respVO.setPublishTime(System.currentTimeMillis());
            return respVO;
        } catch (Exception e) {
            log.error("[publishToXiaohongshu][发布失败]", e);
            ContentPublishRespVO respVO = new ContentPublishRespVO();
            respVO.setSuccess(false);
            respVO.setErrorMessage(e.getMessage());
            return respVO;
        }
        */

        // 临时返回成功（需要实现实际发布逻辑）
        ContentPublishRespVO respVO = new ContentPublishRespVO();
        respVO.setSuccess(true);
        respVO.setPlatformPublishId("temp_" + System.currentTimeMillis());
        respVO.setPublishTime(System.currentTimeMillis());
        return respVO;
    }

    @Override
    public ContentPublishRespVO publishToDouyin(ContentPublishReqVO publishReqVO) {
        log.info("[publishToDouyin][发布到抖音，参数：{}]", publishReqVO);

        // TODO: 实现抖音发布逻辑
        // 这里需要调用抖音开放平台 API 或使用第三方 SDK
        // 实现方式类似小红书

        // 临时返回成功（需要实现实际发布逻辑）
        ContentPublishRespVO respVO = new ContentPublishRespVO();
        respVO.setSuccess(true);
        respVO.setPlatformPublishId("temp_" + System.currentTimeMillis());
        respVO.setPublishTime(System.currentTimeMillis());
        return respVO;
    }

    @Override
    public void updateDataStatus(ContentStatusUpdateReqVO updateReqVO) {
        log.info("[updateDataStatus][更新数据状态，参数：{}]", updateReqVO);

        // TODO: 实现更新 mediaCrawler 数据状态的逻辑
        /*
        mediaCrawlerDataService.updateStatus(
            updateReqVO.getDataId(),
            updateReqVO.getProcessed(),
            updateReqVO.getPublished(),
            updateReqVO.getPublishTime(),
            updateReqVO.getRemark()
        );
        */
    }

    @Override
    public ContentSensitivityCheckRespVO checkSensitivity(String content) {
        log.info("[checkSensitivity][检查敏感词，内容长度：{}]", content != null ? content.length() : 0);

        // TODO: 实现敏感词检测逻辑
        // 可以使用项目中的敏感词服务
        /*
        List<String> sensitiveWords = sensitiveWordService.check(content);
        String safeContent = sensitiveWordService.replace(content);
        
        ContentSensitivityCheckRespVO respVO = new ContentSensitivityCheckRespVO();
        respVO.setHasSensitiveWords(CollUtil.isNotEmpty(sensitiveWords));
        respVO.setSensitiveWords(sensitiveWords);
        respVO.setSafeContent(safeContent);
        return respVO;
        */

        // 临时返回无敏感词
        ContentSensitivityCheckRespVO respVO = new ContentSensitivityCheckRespVO();
        respVO.setHasSensitiveWords(false);
        respVO.setSensitiveWords(Collections.emptyList());
        respVO.setSafeContent(content);
        return respVO;
    }

    // ========== 私有方法 ==========

    /**
     * 构建文案生成提示词
     */
    private String buildTextPrompt(ContentTextGenerateReqVO generateReqVO) {
        StringBuilder prompt = new StringBuilder();
        
        // 平台特定要求
        if ("xiaohongshu".equals(generateReqVO.getPlatform())) {
            prompt.append("请为小红书平台生成一篇笔记，要求：\n");
            prompt.append("1. 标题要吸引眼球，使用 emoji 表情\n");
            prompt.append("2. 正文要有互动性，引导用户点赞、评论、收藏\n");
            prompt.append("3. 内容要真实、有价值\n");
        } else if ("douyin".equals(generateReqVO.getPlatform())) {
            prompt.append("请为抖音平台生成一篇文案，要求：\n");
            prompt.append("1. 标题要简洁有力，能引起共鸣\n");
            prompt.append("2. 正文要有话题性，适合短视频配文\n");
        }

        // 添加额外提示词
        if (StrUtil.isNotBlank(generateReqVO.getExtraPrompt())) {
            prompt.append("\n").append(generateReqVO.getExtraPrompt());
        }

        return prompt.toString();
    }

    /**
     * 构建图片生成提示词
     */
    private String buildImagePrompt(String content) {
        // 从内容中提取关键词，用于生成图片
        return "根据以下内容生成配图：" + content.substring(0, Math.min(200, content.length()));
    }

    /**
     * 从生成内容中提取标题
     */
    private String extractTitle(String content) {
        if (StrUtil.isBlank(content)) {
            return "无标题";
        }
        // 简单提取：取第一行或前 30 个字符
        String[] lines = content.split("\n");
        if (lines.length > 0 && StrUtil.isNotBlank(lines[0])) {
            return lines[0].length() > 50 ? lines[0].substring(0, 50) + "..." : lines[0];
        }
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    /**
     * 提取标签
     */
    private List<String> extractTags(String content, String platform) {
        // TODO: 使用 AI 提取标签，或使用关键词提取算法
        // 临时返回空列表
        return Collections.emptyList();
    }
}
