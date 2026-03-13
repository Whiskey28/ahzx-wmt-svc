---
name: madopic-markdown-to-picture
description: 将 Madopic（Markdown to Picture）集成到当前仓库的可复用工作流：使用本技能自带的 `scripts/` 静态站点文件来启动、配置并导出 PNG/PDF/HTML（图文长图/海报）。也提供可选的 Playwright 自动化导出方案。
---

# Madopic：Markdown 转图文（PNG/PDF/HTML）

Madopic 是一个现代化的 Markdown 可视化工具，可将 Markdown 渲染为精美的图片海报/长图，并支持导出 PNG、PDF、HTML，且支持 KaTeX（数学公式）、Mermaid、ECharts 等能力。项目见 GitHub：[xiaolinbaba/madopic](https://github.com/xiaolinbaba/madopic)。

## 何时使用本技能

- 你已经把 Madopic 的静态站点文件放到本技能自带的 `scripts/` 目录（见下方“目录约定”）
- 需要把一个 `.md` / 一段 Markdown 内容转为：
  - **长图/海报 PNG**（适合朋友圈、小红书等）
  - **PDF**（打印/分享）
  - **HTML**（网页发布或二次加工）
- 需要可调整：背景、字体大小、边距、模式（自由/小红书 3:4/朋友圈长图）等

## 目录约定（必须满足）

本技能约定 Madopic 静态站点文件位于：

- `./.cursor/skills/madopic-markdown-to-picture/scripts/index.html`
- `./.cursor/skills/madopic-markdown-to-picture/scripts/style.css`
- `./.cursor/skills/madopic-markdown-to-picture/scripts/script.js`

若你把文件放在其他位置（例如 `./scripts/madopic/`），调用时同步调整“启动目录/访问 URL”即可。

## 快速开始（人工导出，最稳）

### 方式 A：用 Python 启本地静态服务（推荐）

在仓库根目录执行（端口可改）：

```bash
cd .cursor/skills/madopic-markdown-to-picture/scripts
py -m http.server 5173
```

然后用浏览器打开 `http://localhost:5173/`。

### 方式 B：用任意静态服务器

你也可以用任意静态服务（例如 VSCode Live Server、nginx、http-server 等）把 `./.cursor/skills/madopic-markdown-to-picture/scripts/` 作为站点根目录即可。

## 导出（PNG / PDF / HTML）

在 Madopic 页面中：

- **输入 Markdown**：将你的 Markdown 粘贴到左侧编辑区（或替换原内容）。
- **调整样式**：在“自定义”里调整背景/字体/边距/模式（自由/小红书 3:4/朋友圈长图）。
- **导出**：
  - 点击“导出 PNG”得到图片
  - 点击“导出 PDF”得到矢量文档
  - 点击“导出 HTML”得到 HTML 文件

## 常见需求提示

- **长代码块显示不全**：Madopic 内置了代码块换行/导出优化；尽量避免超大图片宽度不够时的横向滚动。
- **公式/图表导出不完整**：确保导出前渲染完成；如果内容复杂（KaTeX/Mermaid/ECharts），建议等待预览稳定后再导出。
- **社媒尺寸**：优先使用“模式”里的小红书/朋友圈选项，再配合边距与字体大小微调。

## 可选：自动化导出（批量/无人值守）

如果你希望“给定一个 Markdown 文件路径 → 自动生成 PNG”，可采用 Headless 浏览器方案（Playwright/Puppeteer）。下面给出一个 **Playwright** 的推荐流程（更适合 CI/批处理）。

### 自动化思路（不依赖点击 UI）

1. 本地启动本技能 `scripts/` 静态站点（例如 `http://localhost:5173/`）
2. Playwright 打开页面
3. 将 Markdown 注入到编辑器对应的 DOM（或触发页面已有的渲染逻辑）
4. 等待渲染完成
5. 对预览区域截图（或调用页面导出逻辑）生成 PNG

### 最小命令模板（由 Agent 选择性执行）

```bash
# 1) 启动站点（新终端/后台）
cd .cursor/skills/madopic-markdown-to-picture/scripts
py -m http.server 5173

# 2) 在仓库根目录安装并运行 Playwright（只在需要自动化时）
npx playwright install --with-deps chromium
```

接下来编写一个脚本（例如 `tools/madopic-export.mjs`）完成注入与截图。不同版本的 Madopic 编辑器/DOM 结构可能会变化，
脚本需要根据 `./.cursor/skills/madopic-markdown-to-picture/scripts/index.html` 与 `./.cursor/skills/madopic-markdown-to-picture/scripts/script.js`
中的编辑器实现来定位元素（例如 textarea / contenteditable / Monaco 等）。

## Agent 调用检查清单（用于指导下次执行）

- **确认静态站点文件就绪**：存在 `./.cursor/skills/madopic-markdown-to-picture/scripts/index.html`
- **启动本地服务**：能访问 `http://localhost:<port>/`
- **完成导出**：
  - 人工模式：通过页面按钮导出 PNG/PDF/HTML
  - 自动化模式：使用 Playwright 脚本产出 PNG（必要时再转 PDF/HTML）

