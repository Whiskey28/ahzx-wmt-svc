# yt-dlp 参考

与 SKILL.md 配合使用；需要详细选项或模板字段时查阅本文档或 [官方文档](https://github.com/yt-dlp/yt-dlp#readme)。

## 输出模板常用字段

- **id**, **title**, **fulltitle**, **ext**, **uploader**, **uploader_id**, **upload_date**
- **duration**, **duration_string**, **view_count**, **like_count**, **comment_count**
- **channel**, **channel_id**, **description**, **thumbnail**
- **playlist_index**, **playlist**, **playlist_id**, **playlist_title**, **n_entries**
- **series**, **season_number**, **episode**, **episode_number**
- **timestamp**, **release_date**, **webpage_url**

格式：`%(field)s`、`%(field)05d`、`%(field>%Y-%m-%d)s`（strftime）、缺省 `|default`。

## 格式筛选与排序

- 筛选：`[height=720]`、`[height<=720]`、`[ext=mp4]`、`[vcodec^=avc]`、`[filesize<50M]`。字符串可用 `^=`、`$=`、`*=`、`~=`（正则）。未知值用 `?` 如 `[height<=?720]`。
- 排序：`-S res`、`-S "+size"`、`-S "codec:h264"`。字段如 res, size, br, vcodec, acodec, fps, proto, ext。

## 网络与认证

- **--proxy** URL（含 socks5://）
- **--cookies** FILE（Netscape 格式）
- **--cookies-from-browser** BROWSER[:PROFILE]（如 chrome, firefox）
- **--username** / **--password**；**--twofactor** 2FA 码
- **--netrc** / **--netrc-location** / **--netrc-cmd**

## 后处理

- **--embed-subs**：内嵌字幕
- **--embed-thumbnail**：内嵌封面
- **--embed-metadata** / **--add-metadata**：写入元数据
- **--remux-video** FORMAT： remux 容器（如 mp4/mkv）
- **--recode-video** FORMAT：重新编码
- **--postprocessor-args** NAME:ARGS：传给 FFmpeg 等
- **--exec** `[WHEN:]CMD`：在指定阶段执行命令（如 after_move）

## 播放列表与批量

- **-I** / **--playlist-items** ITEM_SPEC：如 `1:3,7` 或 `-1` 从末尾
- **--flat-playlist**：不展开播放列表条目
- **-a** / **--batch-file** FILE：每行一个 URL
- **--download-archive** FILE：跳过已下载 ID
- **--max-downloads** N：最多下载 N 个

## Extractor 参数（示例）

- **--extractor-args "youtube:player_client=web,ios"**
- **--extractor-args "youtube:skip=dash,hls"**
- 其他站点见官方 README 的 “EXTRACTOR ARGUMENTS”。

## 依赖说明

- **ffmpeg** / **ffprobe**：合并、转码、部分后处理（必须为二进制，非 Python 包）
- **yt-dlp-ejs** + JS 运行时（deno/node/bun）：完整 YouTube 支持
- **mutagen**：部分格式的 `--embed-thumbnail`
- **pycryptodomex**：部分 HLS 解密
