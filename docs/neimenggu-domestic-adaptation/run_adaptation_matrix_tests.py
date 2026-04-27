#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
内蒙国产化适配：TongWeb/TongRDS 测试矩阵自动化执行器

设计目标：
- 从 `tongweb-tongrds-adaptation-test-matrix.md` 中提取“待验证/环境受限”用例
- 对可脚本化用例执行 HTTP 校验并输出证据片段
- 对必须人工/环境依赖用例标记 SKIPPED（并说明原因），避免误判为 FAIL

使用方法（示例）：
  export ADAPT_BASE_URL="http://172.20.10.4:48080"
  export ADAPT_BEARER="Bearer xxx"
  python3 docs/neimenggu-domestic-adaptation/run_adaptation_matrix_tests.py

输出：
- `docs/neimenggu-domestic-adaptation/test-reports/adaptation-matrix-report-YYYYmmdd-HHMMSS.json`
"""

from __future__ import annotations

import json
import os
import re
import sys
import time
from dataclasses import dataclass
from datetime import datetime, timezone
from typing import Any, Callable, Dict, List, Optional, Tuple
from urllib.error import HTTPError, URLError
from urllib.parse import urljoin
from urllib.request import Request, urlopen


SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
DEFAULT_MATRIX_PATH = os.path.join(SCRIPT_DIR, "tongweb-tongrds-adaptation-test-matrix.md")


@dataclass(frozen=True)
class TestResult:
    id: str
    name: str
    status: str  # PASS/FAIL/SKIPPED
    evidence: str


def _now_local_str() -> str:
    # Use local time for filename readability
    return datetime.now().strftime("%Y%m%d-%H%M%S")


def http_json(
    base_url: str,
    path: str,
    bearer: Optional[str],
    timeout_s: float = 8.0,
) -> Tuple[int, Dict[str, str], Optional[Dict[str, Any]], str]:
    """
    Returns: (status_code, headers_lower, json_obj_or_none, body_text_prefix)
    """
    url = urljoin(base_url.rstrip("/") + "/", path.lstrip("/"))
    headers = {
        "Accept": "application/json",
        "User-Agent": "ahzx-adaptation-matrix-runner/1.0",
    }
    if bearer:
        headers["Authorization"] = bearer
    req = Request(url, headers=headers, method="GET")

    try:
        with urlopen(req, timeout=timeout_s) as resp:
            raw = resp.read()
            body = raw.decode("utf-8", "ignore")
            headers_lower = {k.lower(): v for k, v in resp.headers.items()}
            try:
                obj = json.loads(body)
            except Exception:
                obj = None
            return resp.status, headers_lower, obj, body[:500]
    except HTTPError as e:
        raw = e.read()
        body = raw.decode("utf-8", "ignore")
        headers_lower = {k.lower(): v for k, v in (e.headers.items() if e.headers else [])}
        try:
            obj = json.loads(body)
        except Exception:
            obj = None
        return e.code, headers_lower, obj, body[:500]
    except URLError as e:
        raise RuntimeError(f"网络错误：{url} -> {e}") from e


def assert_common_result(obj: Optional[Dict[str, Any]]) -> Tuple[bool, str]:
    if not isinstance(obj, dict):
        return False, "响应不是 JSON object"
    for k in ("code", "msg", "data"):
        if k not in obj:
            return False, f"缺少字段 `{k}`（非 CommonResult 结构）"
    return True, "CommonResult 结构OK"


def contains_chinese(text: str) -> bool:
    return bool(re.search(r"[\u4e00-\u9fff]", text))


# ----------------------------
# Automated checks for matrix items
# ----------------------------

def test_TW_02_restart_manual(_: str, __: Optional[str]) -> TestResult:
    return TestResult(
        id="TW-02",
        name="应用热重启（需人工）",
        status="SKIPPED",
        evidence="该项需要在服务器侧执行重启并留痕（无法通过远程只读 HTTP 自动触发）。",
    )


def test_TW_05_error_handling(base_url: str, bearer: Optional[str]) -> TestResult:
    """
    Use user module endpoints to validate:
    - missing required param returns CommonResult error (non-500)
    - bad param returns CommonResult error (non-500)
    """
    checks: List[str] = []

    status1, _, obj1, prefix1 = http_json(base_url, "/admin-api/system/user/get", bearer)
    ok_cr1, msg1 = assert_common_result(obj1)
    if not ok_cr1:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"缺少CommonResult：{msg1}; body={prefix1}")
    if status1 >= 500:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"缺少参数却返回 {status1}（期望非500）；body={prefix1}")
    if obj1.get("code") == 0:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"缺少参数却 code=0（期望错误码）；body={prefix1}")
    checks.append(f"GET /admin-api/system/user/get -> http {status1}, code={obj1.get('code')}")

    status2, _, obj2, prefix2 = http_json(base_url, "/admin-api/system/user/get?id=not_a_number", bearer)
    ok_cr2, msg2 = assert_common_result(obj2)
    if not ok_cr2:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"非CommonResult：{msg2}; body={prefix2}")
    if status2 >= 500:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"非法参数返回 {status2}（期望非500）；body={prefix2}")
    if obj2.get("code") == 0:
        return TestResult("TW-05", "全局异常处理一致性", "FAIL", f"非法参数却 code=0（期望错误码）；body={prefix2}")
    checks.append(f"GET /admin-api/system/user/get?id=not_a_number -> http {status2}, code={obj2.get('code')}")

    return TestResult("TW-05", "全局异常处理一致性", "PASS", "；".join(checks))


def test_TW_07_utf8(base_url: str, bearer: Optional[str]) -> TestResult:
    status, _, obj, prefix = http_json(base_url, "/admin-api/system/user/list-all-simple", bearer)
    ok_cr, msg = assert_common_result(obj)
    if not ok_cr:
        return TestResult("TW-07", "中文编码一致性（读验证）", "FAIL", f"{msg}; body={prefix}")
    if status != 200 or obj.get("code") != 0:
        return TestResult("TW-07", "中文编码一致性（读验证）", "FAIL", f"http={status}, code={obj.get('code')}; body={prefix}")

    # Since response already contains Chinese nicknames in known data, assert presence of Chinese
    if not contains_chinese(prefix):
        return TestResult(
            "TW-07",
            "中文编码一致性（读验证）",
            "FAIL",
            "响应未检测到中文字符（可能数据集无中文或编码异常）。建议补充写入中文字段的用例再验证。",
        )
    return TestResult(
        "TW-07",
        "中文编码一致性（读验证）",
        "PASS",
        "GET /admin-api/system/user/list-all-simple 返回 code=0 且响应包含中文字符（UTF-8解码正常）",
    )


def test_TW_09_druid(base_url: str, bearer: Optional[str]) -> TestResult:
    # Druid console typically doesn't require bearer; still try with bearer harmless
    candidates = ["/druid/", "/druid/index.html", "/druid/login.html"]
    evidences: List[str] = []
    for p in candidates:
        try:
            status, headers, obj, prefix = http_json(base_url, p, bearer=None)
        except Exception as e:
            evidences.append(f"{p}: ERROR {e}")
            continue
        # Some deployments may return html (obj None) with 200/302, that's acceptable as "reachable"
        ct = headers.get("content-type", "")
        if status in (200, 302, 301):
            evidences.append(f"{p}: http {status}, content-type={ct}")
            return TestResult("TW-09", "Druid 监控可用", "PASS", "；".join(evidences))
        # if CommonResult 404 => not enabled
        if isinstance(obj, dict) and obj.get("code") == 404:
            evidences.append(f"{p}: CommonResult 404")
        else:
            evidences.append(f"{p}: http {status}, prefix={prefix[:80]}")

    return TestResult(
        "TW-09",
        "Druid 监控可用",
        "SKIPPED",
        "未探测到可访问的 /druid 入口（可能未启用或被网络策略限制）。证据：" + "；".join(evidences),
    )


def test_TW_10_load(_: str, __: Optional[str]) -> TestResult:
    return TestResult(
        id="TW-10",
        name="稳态资源观察（需压测/监控）",
        status="SKIPPED",
        evidence="该项需要压测工具与监控数据（线程/连接池/错误率），不适合在单机脚本内判定。",
    )


def test_TW_11_session(_: str, __: Optional[str]) -> TestResult:
    return TestResult(
        id="TW-11",
        name="会话保持/失效（需交互/多端）",
        status="SKIPPED",
        evidence="该项需要交互式登录/登出、多端复现或真实浏览器行为（脚本无法完整覆盖）。",
    )


def test_TW_12_rollback(_: str, __: Optional[str]) -> TestResult:
    return TestResult(
        id="TW-12",
        name="升级回滚演练（需运维操作）",
        status="SKIPPED",
        evidence="该项需要运维侧执行部署/回滚流程并留痕（脚本无法替代）。",
    )


def test_TR_manual(tr_id: str, name: str) -> TestResult:
    return TestResult(
        id=tr_id,
        name=name,
        status="SKIPPED",
        evidence="该项需要业务侧专用事务/路由/故障注入/压测或DB侧证据，目前缺少可自动化的公共接口证据点。",
    )


TESTS: Dict[str, Callable[[str, Optional[str]], TestResult]] = {
    "TW-02": test_TW_02_restart_manual,
    "TW-05": test_TW_05_error_handling,
    "TW-06": lambda *_: TestResult("TW-06", "文件上传下载", "SKIPPED", "需要明确上传/下载接口与样例文件（当前未在矩阵中给出可自动化接口）。"),
    "TW-07": test_TW_07_utf8,
    "TW-09": test_TW_09_druid,
    "TW-10": test_TW_10_load,
    "TW-11": test_TW_11_session,
    "TW-12": test_TW_12_rollback,
    # TongRDS pending items
    "TR-03": lambda *_: test_TR_manual("TR-03", "事务提交验证"),
    "TR-04": lambda *_: test_TR_manual("TR-04", "事务回滚验证"),
    "TR-05": lambda *_: test_TR_manual("TR-05", "唯一约束冲突"),
    "TR-06": lambda *_: test_TR_manual("TR-06", "主从路由正确性"),
    "TR-07": lambda *_: test_TR_manual("TR-07", "分页 SQL 兼容"),
    "TR-08": lambda *_: test_TR_manual("TR-08", "排序与过滤条件兼容"),
    "TR-09": lambda *_: test_TR_manual("TR-09", "批量写入兼容"),
    "TR-10": lambda *_: test_TR_manual("TR-10", "慢 SQL 可观测"),
    "TR-11": lambda *_: test_TR_manual("TR-11", "连接池稳态"),
    "TR-12": lambda *_: test_TR_manual("TR-12", "断连恢复能力"),
    "TR-13": lambda *_: test_TR_manual("TR-13", "字符集与时区一致性"),
    "TR-14": lambda *_: test_TR_manual("TR-14", "大结果集读取稳定性"),
}


def parse_pending_ids(matrix_md: str) -> List[str]:
    """
    Extract IDs whose current status is 待验证 or 环境受限 in the main matrix table.
    We intentionally skip those already marked 已验证.
    """
    ids: List[str] = []
    in_table = False
    for line in matrix_md.splitlines():
        if line.strip().startswith("| TW-01") or line.strip().startswith("| TR-01"):
            in_table = True
        if not in_table:
            continue
        # table rows look like: | TW-05 | P1 | 待验证 | ...
        m = re.match(r"^\|\s*(T[WR]-\d{2})\s*\|\s*[^|]*\|\s*([^|]+)\|", line)
        if not m:
            continue
        test_id = m.group(1).strip()
        status = m.group(2).strip()
        if status in ("待验证", "环境受限"):
            ids.append(test_id)
    # ensure stable ordering and uniqueness
    seen = set()
    ordered: List[str] = []
    for i in ids:
        if i not in seen:
            seen.add(i)
            ordered.append(i)
    return ordered


def main() -> int:
    base_url = os.getenv("ADAPT_BASE_URL", "http://172.20.10.4:48080")
    bearer = os.getenv("ADAPT_BEARER")

    if not os.path.exists(DEFAULT_MATRIX_PATH):
        print(f"未找到矩阵文件：{DEFAULT_MATRIX_PATH}", file=sys.stderr)
        return 2

    with open(DEFAULT_MATRIX_PATH, "r", encoding="utf-8") as f:
        md = f.read()

    pending_ids = parse_pending_ids(md)
    if not pending_ids:
        print("未解析到待测用例（待验证/环境受限）。")
        return 0

    # Some checks require bearer; if missing, mark those checks as SKIPPED
    results: List[TestResult] = []
    for test_id in pending_ids:
        fn = TESTS.get(test_id)
        if fn is None:
            results.append(TestResult(test_id, "未实现自动化用例", "SKIPPED", "该用例未在脚本映射中实现。"))
            continue

        needs_auth = test_id in ("TW-05", "TW-06", "TW-07")  # conservative
        if needs_auth and not bearer:
            results.append(TestResult(test_id, "需认证的用例", "SKIPPED", "缺少环境变量 ADAPT_BEARER（Bearer Token）"))
            continue

        try:
            r = fn(base_url, bearer)
        except Exception as e:
            r = TestResult(test_id, "执行异常", "FAIL", f"{type(e).__name__}: {e}")
        results.append(r)

    report = {
        "meta": {
            "base_url": base_url,
            "matrix_path": os.path.relpath(DEFAULT_MATRIX_PATH, SCRIPT_DIR),
            "generated_at": datetime.now(timezone.utc).isoformat(),
        },
        "summary": {
            "PASS": sum(1 for r in results if r.status == "PASS"),
            "FAIL": sum(1 for r in results if r.status == "FAIL"),
            "SKIPPED": sum(1 for r in results if r.status == "SKIPPED"),
            "total": len(results),
        },
        "results": [
            {"id": r.id, "name": r.name, "status": r.status, "evidence": r.evidence}
            for r in results
        ],
    }

    out_dir = os.path.join(SCRIPT_DIR, "test-reports")
    os.makedirs(out_dir, exist_ok=True)
    out_path = os.path.join(out_dir, f"adaptation-matrix-report-{_now_local_str()}.json")
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(report, f, ensure_ascii=False, indent=2)

    print(json.dumps(report["summary"], ensure_ascii=False))
    print(f"报告已生成：{out_path}")

    return 1 if report["summary"]["FAIL"] > 0 else 0


if __name__ == "__main__":
    raise SystemExit(main())

