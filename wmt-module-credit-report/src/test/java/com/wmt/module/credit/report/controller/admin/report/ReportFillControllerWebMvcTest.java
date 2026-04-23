package com.wmt.module.credit.report.controller.admin.report;

import com.wmt.framework.common.biz.infra.logger.ApiErrorLogCommonApi;
import com.wmt.framework.web.config.WmtWebAutoConfiguration;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportCategoryRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportTemplateRespVO;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import com.wmt.module.credit.report.service.ReportFillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 征信报送 - 报表填报 Controller 层契约测试（不依赖数据库/Redis）。
 * <p>需 JDK 17，示例：{@code mvn -pl wmt-module-credit-report -am test "-Dtest=ReportFillControllerWebMvcTest" "-Dsurefire.failIfNoSpecifiedTests=false"}
 */
@WebMvcTest(controllers = ReportFillController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({WmtWebAutoConfiguration.class, RestTemplateAutoConfiguration.class})
@TestPropertySource(
        properties = {
                "spring.application.name=wmt-server-test",
                "wmt.web.admin-ui.url=http://localhost"
        })
class ReportFillControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportFillService reportFillService;

    @MockBean
    private ApiErrorLogCommonApi apiErrorLogCommonApi;

    @Test
    void getCategoryList_returnsCommonResult() throws Exception {
        JimuReportCategoryRespVO vo = new JimuReportCategoryRespVO();
        vo.setId("101");
        vo.setName("测试分类");
        vo.setParentId("0");
        when(reportFillService.getCategoryList()).thenReturn(List.of(vo));

        mockMvc.perform(get("/admin-api/credit/report-fill/category/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].id").value("101"))
                .andExpect(jsonPath("$.data[0].name").value("测试分类"));
    }

    @Test
    void getFillRecordPage_whenNoFilterParams_returnsEmptyPage() throws Exception {
        mockMvc.perform(get("/admin-api/credit/report-fill/record/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.list").isEmpty());
    }

    @Test
    void getTemplateList_withCategoryId_returnsCommonResult() throws Exception {
        JimuReportTemplateRespVO vo = new JimuReportTemplateRespVO();
        vo.setId("tpl-1");
        vo.setName("模板A");
        when(reportFillService.getTemplateListByCategoryId("cat-9", 1)).thenReturn(List.of(vo));

        mockMvc.perform(get("/admin-api/credit/report-fill/template/list")
                        .param("categoryId", "cat-9")
                        .param("submitForm", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].id").value("tpl-1"))
                .andExpect(jsonPath("$.data[0].name").value("模板A"));
    }
}
