package com.wmt.module.ticket.service.bpm.util;

/**
 * 流程发起参数中的「流程定义 Key」规范化：支持误粘贴 Flowable 完整 definitionId（key:version:deploymentId）。
 */
public final class BpmProcessDefinitionKeyUtils {

    private BpmProcessDefinitionKeyUtils() {
    }

    /**
     * 将误粘贴的完整 ID 规范为 Key；若已是纯 Key 则原样返回。
     */
    public static String normalize(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        String s = raw.trim();
        int last = s.lastIndexOf(':');
        if (last <= 0) {
            return s;
        }
        int second = s.lastIndexOf(':', last - 1);
        if (second < 0) {
            return s;
        }
        String version = s.substring(second + 1, last);
        if (!version.chars().allMatch(Character::isDigit)) {
            return s;
        }
        return s.substring(0, second);
    }
}
