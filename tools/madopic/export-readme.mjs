import fs from 'node:fs/promises';
import path from 'node:path';
import process from 'node:process';
import { fileURLToPath } from 'node:url';
import { chromium } from 'playwright';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const repoRoot = path.resolve(__dirname, '..', '..');
const readmePath = path.join(repoRoot, 'README.md');
const outPath = path.join(repoRoot, 'README.png');

const serverUrl = process.env.MADOPIC_URL || 'http://127.0.0.1:5173/';

async function main() {
  const markdown = await fs.readFile(readmePath, 'utf8');

  const browser = await chromium.launch({
    headless: true,
  });
  const context = await browser.newContext({
    acceptDownloads: true,
    viewport: { width: 1400, height: 900 },
    deviceScaleFactor: 2,
  });
  const page = await context.newPage();

  // 让静态站点的相对资源、CDN 依赖在 HTTP 下正常工作
  await page.goto(serverUrl, { waitUntil: 'domcontentloaded' });

  // 等待应用初始化（window.MadopicApp 会在 script.js 末尾挂出来）
  await page.waitForFunction(() => typeof window.MadopicApp?.updatePreview === 'function', null, {
    timeout: 30_000,
  });

  // 注入 Markdown 到 textarea，并触发 input 事件，驱动预览渲染
  await page.evaluate((md) => {
    const textarea = document.querySelector('#markdownInput');
    if (!textarea) throw new Error('找不到 #markdownInput');
    textarea.value = md;
    textarea.dispatchEvent(new Event('input', { bubbles: true }));
  }, markdown);

  // 等待渲染完成：posterContent 不再是空，并且图片（若有）尽量加载完成
  await page.waitForFunction(() => {
    const el = document.querySelector('#posterContent');
    if (!el) return false;
    const hasContent = (el.textContent || '').trim().length > 0 || el.querySelector('*');
    return Boolean(hasContent);
  });

  await page.waitForTimeout(800); // 给 KaTeX/Mermaid/Prism 等异步渲染留一点时间

  // 点击“导出 PNG”，捕获下载，并保存为根目录 README.png
  const downloadPromise = page.waitForEvent('download', { timeout: 120_000 });
  await page.click('#exportPngBtn');
  const download = await downloadPromise;
  await download.saveAs(outPath);

  await browser.close();
  // 让 CI / 调用者能在 stdout 看到产物路径
  process.stdout.write(`${outPath}\n`);
}

await main();

