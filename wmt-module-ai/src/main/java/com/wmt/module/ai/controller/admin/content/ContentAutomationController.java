package com.wmt.module.ai.controller.admin.content;

import com.wmt.framework.common.pojo.CommonResult;
import com.wmt.module.ai.controller.admin.content.vo.*;
import com.wmt.module.ai.service.content.ContentAutomationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wmt.framework.common.pojo.CommonResult.success;

/**
 * 内容自动化 Controller
 * 用于 n8n 工作流调用，实现从 mediaCrawler 数据到内容生成和发布的自动化流程
 *
 * @author Auto Generated
 */
@Tag(name = "管理后台 - 内容自动化")
@RestController
@RequestMapping("/api/content")
@Slf4j
public class ContentAutomationController {

    @Resource
    private ContentAutomationService contentAutomationService;

    @PostMapping("/crawler/query")
    @Operation(summary = "查询 mediaCrawler 数据", description = "从 mediaCrawler 数据库查询未处理的数据，供 n8n 工作流使用")
    @PreAuthorize("@ss.hasPermission('ai:content:query')")
    public CommonResult<List<CrawlerDataRespVO>> queryCrawlerData(@Valid @RequestBody CrawlerDataQueryReqVO queryReqVO) {
        return success(contentAutomationService.queryCrawlerData(queryReqVO));
    }

    @PostMapping("/generate/text")
    @Operation(summary = "AI 生成文案", description = "基于 mediaCrawler 数据生成小红书/抖音文案")
    @PreAuthorize("@ss.hasPermission('ai:content:generate')")
    public CommonResult<ContentGenerateRespVO> generateText(@Valid @RequestBody ContentTextGenerateReqVO generateReqVO) {
        return success(contentAutomationService.generateText(generateReqVO));
    }

    @PostMapping("/generate/image")
    @Operation(summary = "AI 生成图片", description = "基于文案生成配图")
    @PreAuthorize("@ss.hasPermission('ai:content:generate')")
    public CommonResult<ContentImageGenerateRespVO> generateImage(@Valid @RequestBody ContentImageGenerateReqVO generateReqVO) {
        return success(contentAutomationService.generateImage(generateReqVO));
    }

    @PostMapping("/generate")
    @Operation(summary = "AI 生成完整内容", description = "同时生成文案和图片")
    @PreAuthorize("@ss.hasPermission('ai:content:generate')")
    public CommonResult<ContentFullGenerateRespVO> generateFullContent(@Valid @RequestBody ContentFullGenerateReqVO generateReqVO) {
        return success(contentAutomationService.generateFullContent(generateReqVO));
    }

    @PostMapping("/publish/xiaohongshu")
    @Operation(summary = "发布到小红书", description = "将生成的内容发布到小红书平台")
    @PreAuthorize("@ss.hasPermission('ai:content:publish')")
    public CommonResult<ContentPublishRespVO> publishToXiaohongshu(@Valid @RequestBody ContentPublishReqVO publishReqVO) {
        return success(contentAutomationService.publishToXiaohongshu(publishReqVO));
    }

    @PostMapping("/publish/douyin")
    @Operation(summary = "发布到抖音", description = "将生成的内容发布到抖音平台")
    @PreAuthorize("@ss.hasPermission('ai:content:publish')")
    public CommonResult<ContentPublishRespVO> publishToDouyin(@Valid @RequestBody ContentPublishReqVO publishReqVO) {
        return success(contentAutomationService.publishToDouyin(publishReqVO));
    }

    @PostMapping("/update-status")
    @Operation(summary = "更新数据状态", description = "更新 mediaCrawler 数据的处理状态")
    @PreAuthorize("@ss.hasPermission('ai:content:update')")
    public CommonResult<Boolean> updateDataStatus(@Valid @RequestBody ContentStatusUpdateReqVO updateReqVO) {
        contentAutomationService.updateDataStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/check-sensitivity")
    @Operation(summary = "检查敏感词", description = "检查内容是否包含敏感词")
    @PreAuthorize("@ss.hasPermission('ai:content:check')")
    @Parameter(name = "content", description = "待检查的内容", required = true)
    public CommonResult<ContentSensitivityCheckRespVO> checkSensitivity(@RequestParam("content") String content) {
        return success(contentAutomationService.checkSensitivity(content));
    }
}
