# 簡介
* 研究 JDownloader 機制

# Source Code

svn checkout svn://svn.jdownloader.org/jdownloader/trunk

# 原始碼目錄結構

```
/artwork 素材檔
/bin
/build
/dev
/ressourcen
/src
    /javax
    /jd
        /plugins 外掛
            /decrypter 解析真正的連結
            
    /org
        /jdownloader
/tools 第三方工具
/themes 佈景
/translations 語系檔
- API_Specs.txt
```

# 佈署後目錄結構

```
/cfg ; 配置
    /plugins
    
/folderwatch ; Folder Watch 擴展目錄
/extensions ; 擴展工具
    - AntiShutdown.jar 
    - EventScripter.jar
    - Extraction.jar
    - FolderWatch.jar
    - JDShutdown.jar
/java
/jd
    /captcha
    /plugins
        /decrypter
        /hoster
/jre ; java runtime
/libs ; 第三方 Lib
/licenses ; 授權宣告
/logs ; 日誌
/themes ; 佈景
/tmp 
    /extensioncache
        - extensionInfos.json ; 擴展配置
/tools ; 第三方工具
/translations ; 語系檔
/update ; 版本資訊
- build.json ; 建置版本資訊
- Core.jar
- JDownloader.jar
- JD2.lock
- JD2.port
- JDownloader.pid
- JDownloader2.exe
- JDownloader2.vmoptions
- JDownloader2Update.exe
- JDownloader2Update.vmoptions
- license.txt
- license_german.txt
- error.log ; 錯誤訊息日誌
- output.log
```


# DecrypterPlugin
* package = jd.plugins.decrypter
* 繼承 antiDDoSForDecrypt
* 實作
    * hasCaptcha
    * hasAutoCaptcha
    * decryptIt
# Event Scripter
* 監聽 JDownloader 事件並執行對應 js

# Extension: [nemec/JDHttpAPI: JDownloader2 plugin with a local HTTP API to add new files for download](https://github.com/nemec/JDHttpAPI)
* 內嵌 Jetty HTTP server
* 提供 HTTP API 新增連結

## 安裝
1. https://github.com/nemec/JDHttpAPI/releases 下載 JDHttpAPI.jar
2. jar 放在 extensions 
3. tmp/extensioncache/extensionInfos.json 加入如下片段, jarPath 依據安裝環境調整

```
{
  "settings" : true,
  "configInterface" : "org.jdownloader.extensions.httpAPI.HttpAPIConfig",
  "quickToggle" : false,
  "headlessRunnable" : true,
  "description" : "Http API.",
  "lng" : "en_US",
  "iconPath" : "folder_add",
  "linuxRunnable" : true,
  "macRunnable" : true,
  "name" : "HTTP API",
  "version" : -1,
  "windowsRunnable" : true,
  "classname" : "org.jdownloader.extensions.httpAPI.HttpAPIExtension",
  "jarPath" : "/absolute/path/to/jd2/extensions/JDHttpApi.jar"
}
```
4. update/versioninfo/JD/extensions.installed.json 新增 "jdhttpapi"
5. 啟動 JDownloader
6. 設定中啟用 JDHttpAPI

# FAQ : 取得解析後的真實下載連結
* /cfg/linkcollector*.zip
    * extrainfo - 包含最後更新日期
    * \d{2} - 資料夾資訊
    * \d{2}_\d{2} - 檔案資訊
        * 第一碼與目錄關聯
* jd.plugins.DownloadLink.getContentUrl()
    * jd.plugins.DownloadLink.getOriginUrl()
* jd.controlling.linkcrawler.CrawledLink.getDownloadLink() : DownloadLink
* LinkCollector -> Package -> List<CrawledLink> -> List<DownloadLink>

# 參考資料
* [The JDownloader Open Source Project on Open Hub](https://www.openhub.net/p/jdownloader)
* [The JDownloader Open Source Project on Open Hub : Code Locations Page](https://www.openhub.net/p/jdownloader/enlistments)
* [JDownloader.org - Official Homepage](http://jdownloader.org/knowledge/wiki/development/get-started)
* [JDownloader - Development](http://beta.jdownloader.org/development)