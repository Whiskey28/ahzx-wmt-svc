---
name: yt-dlp-video-downloader
description: Guides use of yt-dlp for downloading and extracting audio/video from YouTube and thousands of other sites. Use when the user asks about video download, audio extraction, yt-dlp, youtube-dl, format selection, subtitles, or embedding a downloader in code.
---

# yt-dlp 视频/音频下载

yt-dlp 是功能丰富的命令行音视频下载器，支持数千个站点（youtube-dl 的活跃分支）。见 [GitHub](https://github.com/yt-dlp/yt-dlp)。

## 何时使用本技能

- 用户提到 **yt-dlp**、**youtube-dl**、视频下载、音频提取
- 需要从 **YouTube** 或其他支持的站点下载视频/音频
- 需要 **格式选择**、**字幕**、**后处理**（如转码/合并）的用法
- 需要在 **Python 或脚本中嵌入** 下载逻辑

## 安装

- **Windows**: 下载 [yt-dlp.exe](https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp.exe) 或 `pip install yt-dlp`
- **macOS**: `brew install yt-dlp` 或同上 pip
- **Linux**: `pip install yt-dlp` 或发行版包管理器
- 更新: `yt-dlp -U` 或 `pip install -U yt-dlp`

**强烈建议** 安装 **ffmpeg**（合并音视频、后处理）和 **yt-dlp-ejs** + JS 运行时（如 deno）以完整支持 YouTube。

## 常用 CLI 用法

```bash
# 默认下载（最佳画质，自动合并音视频）
yt-dlp "https://www.youtube.com/watch?v=VIDEO_ID"

# 仅提取音频（需 ffmpeg）
yt-dlp -x --audio-format mp3 "URL"
yt-dlp -x --audio-format m4a "URL"

# 指定输出模板
yt-dlp -o "%(title)s [%(id)s].%(ext)s" "URL"
yt-dlp -o "%(uploader)s/%(title)s.%(ext)s" "URL"

# 只下载字幕
yt-dlp --write-subs --sub-langs en,zh-Hans --skip-download "URL"

# 模拟/列出格式（不下载）
yt-dlp -F "URL"                    # 列出格式
yt-dlp --print "%(title)s" "URL"   # 打印元数据
yt-dlp -s "URL"                    # 模拟下载
```

## 格式选择 (-f / --format)

- `best` 或默认：最佳含音视频的单一格式，或 bestvideo+bestaudio 合并
- `bv*+ba/b`：最佳视频 + 最佳音频合并（默认策略）
- `-f 22`：指定格式 ID（先 `-F` 查看）
- `-f "best[height<=720]"`：最佳但不高于 720p
- `-f "bv*+ba/b"`：最佳视频+最佳音频，无则退化为 best
- 仅音频：`-f bestaudio` 或 `-f ba`；提取为文件用 `-x --audio-format FORMAT`

常用预设：`-t mp3`、`-t aac`、`-t mp4`、`-t mkv`（见 `--help` 的 Preset Aliases）。

## 输出模板 (-o)

常用占位符：`%(id)s`、`%(title)s`、`%(ext)s`、`%(uploader)s`、`%(upload_date)s`、`%(playlist_index)s`、`%(duration)s`。  
可用 `%(field)05d` 等格式化；缺失时用 `--output-na-placeholder TEXT`（默认 `NA`）。  
路径可含目录，如 `-o "%(playlist)s/%(title)s.%(ext)s"`。

## Python 嵌入

```python
import yt_dlp

URL = 'https://www.youtube.com/watch?v=BaW_jenozKc'

# 仅提取信息（不下载）
with yt_dlp.YoutubeDL({}) as ydl:
    info = ydl.extract_info(URL, download=False)
    # 可序列化: ydl.sanitize_info(info)

# 下载
with yt_dlp.YoutubeDL({'outtmpl': '%(title)s.%(ext)s'}) as ydl:
    ydl.download([URL])

# 仅音频
opts = {
    'format': 'm4a/bestaudio/best',
    'postprocessors': [{'key': 'FFmpegExtractAudio', 'preferredcodec': 'm4a'}]
}
with yt_dlp.YoutubeDL(opts) as ydl:
    ydl.download([URL])
```

选项与 CLI 对应（如 `format`、`outtmpl`、`postprocessors`）。完整选项见 `help(yt_dlp.YoutubeDL)` 或 [官方文档](https://github.com/yt-dlp/yt-dlp#usage-and-options)。

## 常用选项速查

| 选项 | 说明 |
|------|------|
| `-x` / `--extract-audio` | 提取音频 |
| `--audio-format FORMAT` | 音频格式（mp3, m4a, aac, opus 等） |
| `-o` / `--output TEMPLATE` | 输出文件名模板 |
| `-f` / `--format FORMAT` | 格式选择表达式 |
| `-F` | 列出可用格式 |
| `-s` / `--simulate` | 不下载，仅模拟 |
| `--write-subs` / `--write-auto-subs` | 写入字幕 / 自动生成字幕 |
| `--sub-langs LANGS` | 字幕语言（如 en,zh-Hans 或 all） |
| `-P` / `--paths TYPE:PATH` | 按类型设置路径（home/temp 等） |
| `--proxy URL` | HTTP(S)/SOCKS 代理 |
| `--cookies FILE` | Netscape 格式 cookie 文件 |
| `-q` / `--quiet` | 安静模式 |
| `-v` / `--verbose` | 详细输出 |

## 配置与兼容

- 配置文件：`yt-dlp.conf`（当前目录、用户目录或 `--config-locations`）。选项与命令行一致，一行一个。
- 与 youtube-dl 差异：默认格式排序、输出模板等不同；可用 `--compat-options` 恢复部分旧行为。

## 更多说明

- 完整选项与 OUTPUT TEMPLATE 字段见 [reference.md](reference.md)。
- 支持站点列表与更新说明见 [GitHub README](https://github.com/yt-dlp/yt-dlp) 与 [Changelog](https://github.com/yt-dlp/yt-dlp/blob/master/Changelog.md)。
