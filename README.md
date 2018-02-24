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
            - DownloadLink.java
            - 
        /controlling
            /linkcrawler
                - CrawledLink.java
    /org
        /appwork
            /remoteapi
                - RemoteAPI.java
                - APIQuery.java
            /remotecall
            /utils
                /net
                    /httpserver
                        - HttpServer.java
        /jdownloader
            /api
                /linkcollector
                    - LinkCollectorAPI.java
                    - LinkCollectorAPIImpl.java
            /phantomjs
                - PhantomJS.java
            /downloader
                /hls
                    - HLSDownloader.java
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
* org.jdownloader.extensions.eventscripter.sandboxobjects.ScriptEnvironment 實作腳本呼叫 api

## 工具列按鈕執行腳本
* 工具列新增 "Event Scripter Trigger" 按鈕
* 腳本關聯"Event Scripter Trigger" 按鈕

## 常數
* JD_HOME : JDownloader 安裝路徑

## function

### alert(字串)
* UI 提示

### callAPI(namespace, method, 參數...)
* 下載列表
    * callAPI("downloadsV2", "queryLinks", { "name": true})
        * namespace = downloadsV2 => 對應 /src/org/jdownloader/api/downloads/v2/DownloadsAPIV2.java 中的 @ApiNamespace        
        * method = queryLinks => 對應 class method
        * params = { "name": true} => 對應 @APIParameterNames
* 實際調用 org.jdownloader.api.RemoteAPIController.call()

### callAsync( function(exitCode,stdOut,errOut){}, 程式完整路徑, 參數)
* 非同步呼叫外部程式
* callAsync(function(exitCode,stdOut,errOut){ alert("Closed Notepad");},"notepad.exe",JD_HOME+"\\license.txt");

### callSync(程式完整路徑, 參數)
* 同步呼叫外部程式, 直到外部程式結束返回
* var pingResultString = callSync("ping","jdownloader.org");

### deleteFile(檔案路徑, 是否遞迴子目錄)
* 刪除指定檔案/目錄
* var myBooleanResult=deleteFile(JD_HOME+"/mydirectory/",false);

### disablePermissionChecks()/enablePermissionChecks()
* 關閉/啟動權限檢查提醒

### requestReconnect()
* 非同步重新連線

### doReconnect()
* 重新連線同步等待執行結果
* var success= doReconnect();

### getRunningDownloadLinks()
* 取得執行中的 DownloadLink 列表

### getRunningDownloadPackages()
* 取得執行中的 DownloadPackage 列表

### getAllCrawledLinks()
* 取得 crawledlinks 列表

### getAllCrawledPackages()
* 取得 CrawledPackage 列表

### getAllDownloadLinks()
* 取得 DownloadLink 列表

### getAllFilePackages()
* 取得 FilePackage 列表

### setSpeedlimit( bytes/s )
* 0 表示不限制

### getAverageSpeed()
* 取得目前平均速度
### getTotalSpeed()
* 取得總下載速度 bytes/s

### isDownloadControllerIdle()
* 下載處於閒置狀態

### isDownloadControllerPaused()
* 下載處於暫停狀態

### isDownloadControllerRunning()
* 下載處於執行狀態

### isDownloadControllerStopping()
* 下載處於停止狀態

### playWavAudio(wav 完整路徑)
* 播放 wav 檔


### getEnv(key)
* 取得指定 key 的環境變數值

### getBrowser()
* 取得 Browser

### getEnvironment()
* 取得 Enviroment

### openURL(url)
* 預設瀏覽器開啟指定網址

### getPage(url)
* HTTP Get 取得內容
* var myhtmlSourceString=getPage("http://jdownloader.org");

### postPage(url, post_data)
* HTTP Post
* var myhtmlSourceString=postPage("http://support.jdownloader.org/index.php","searchquery=captcha");

### require( url )
* 載入外部 url

### writeFile(檔案路徑, 內容, is_append)
* 寫入檔案內容

### readFile(檔案路徑)
* 取得檔案內容

### getPath(檔案/目錄路徑)
* 取得 FilePath

### setProperty(key, value, is_global)
* var oldValue=setProperty("myobject", { "name": true}, false);

### getProperty(key, is_global)
* 取得 Property

### refreshAccounts( is_wait_account_checks, is_force_check)
* refreshAccounts(true, false)
* 重載 premium 帳號

### getChecksum(檔案路徑, hash_method)
* hash : CRC32, md5, SHA-1, SHA-256
* 取得檔案雜湊值

### log(...)
* 寫入 JDownloader 日誌

### startDownloads()/stopDownloads()/setDownloadsPaused( is_pause )
* 啟動/停止/暫停下載

### sleep(ms)
* 沉睡

### getCrawledLinkByUUID( uuid)
* 取得 CrawledLink
 
### getCrawledPackageByUUID( uuid)
* 取得 CrawledPackage
 
### getDownloadLinkByUUID( uuid)
* 取得 DownloadLink
 
### getDownloadPackageByUUID( uuid)
* 取得 DownloadPackage
 
### removeCrawledLinkByUUID( uuid )
* 移除 CrawledLink

### removeCrawledPackageByUUID( uuid )
* 移除 CrawledPackage

### removeDownloadLinkByUUID( uuid )
* 移除 DownloadLink

### removeFilePackageByUUID( uuid )
* 移除 FilePackage

## object

### Enviroment
var myString = myEnvironment.getARCHFamily();
var myLong = myEnvironment.getJavaVersion();
var myString = myEnvironment.getNewLine();
var myString = myEnvironment.getOS();
var myString = myEnvironment.getOSFamily();
var myString = myEnvironment.getPathSeparator();
var myBoolean = myEnvironment.is64BitArch();
var myBoolean = myEnvironment.is64BitJava();
var myBoolean = myEnvironment.is64BitOS();
var myBoolean = myEnvironment.isBSD();
var myBoolean = myEnvironment.isHeadless();
var myBoolean = myEnvironment.isLinux();
var myBoolean = myEnvironment.isMac();
var myBoolean = myEnvironment.isWindows();

### FilePackage
var myBoolean = myFilePackage.equals(myObject);
var myLong = myFilePackage.getAddedDate();
var myArchive[] = myFilePackage.getArchives();
var myLong = myFilePackage.getBytesLoaded();
var myLong = myFilePackage.getBytesTotal();
var myString = myFilePackage.getComment();
var myString = myFilePackage.getDownloadFolder();
var myDownloadLink[] = myFilePackage.getDownloadLinks();
var myLong = myFilePackage.getFinishedDate();
var myString = myFilePackage.getName();
var myString = myFilePackage.getPriority();
var myString = myFilePackage.getUUID();
var myInt = myFilePackage.hashCode();
var myBoolean = myFilePackage.isFinished();
var myBoolean = myFilePackage.remove();
myFilePackage.setComment(myString);
myFilePackage.setDownloadFolder(myString);
myFilePackage.setName(myString);
myFilePackage.setPriority(myString);
var myString = myFilePackage.toString();

### FilePath
var myBoolean = myFilePath.copyTo(myString);
var myBoolean = myFilePath.delete();
var myBoolean = myFilePath.deleteRecursive();
var myBoolean = myFilePath.equals(myObject);
var myBoolean = myFilePath.exists();
var myString = myFilePath.getAbsolutePath();
var myFilePath[] = myFilePath.getChildren();
var myLong = myFilePath.getCreatedDate();
var myString = myFilePath.getExtension();
var myLong = myFilePath.getModifiedDate();
var myString = myFilePath.getName();
var myFilePath = myFilePath.getParent();
var myString = myFilePath.getPath();
var myLong = myFilePath.getSize();
var myInt = myFilePath.hashCode();
var myBoolean = myFilePath.isDirectory();
var myBoolean = myFilePath.isFile();
var myBoolean = myFilePath.mkdirs();
var myBoolean = myFilePath.moveTo(myString);
var myBoolean = myFilePath.renameTo(myString);
var myString = myFilePath.toString();

### CrawledLink

var myBoolean = myCrawledLink.equals(myObject);
var myLong = myCrawledLink.getAddedDate();
var myArchive = myCrawledLink.getArchive();
var myString = myCrawledLink.getAvailableState();
var myLong = myCrawledLink.getBytesTotal();
var myString = myCrawledLink.getComment();
var myString = myCrawledLink.getContainerURL();
var myString = myCrawledLink.getContentURL();
var myString = myCrawledLink.getDownloadPath();
var myString = myCrawledLink.getHost();
var myString = myCrawledLink.getName();
var myString = myCrawledLink.getOriginURL();
var myCrawledPackage = myCrawledLink.getPackage();
var myString = myCrawledLink.getPriority();
var myObject = myCrawledLink.getProperty(myString);
var myString = myCrawledLink.getReferrerURL();
var myObject = myCrawledLink.getSessionProperty(myString);
var myString = myCrawledLink.getUUID();
var myString = myCrawledLink.getUrl();
var myInt = myCrawledLink.hashCode();
var myBoolean = myCrawledLink.isEnabled();
var myBoolean = myCrawledLink.remove();
myCrawledLink.setComment(myString);
myCrawledLink.setEnabled(myBoolean);
myCrawledLink.setName(myString/*new Name*/);/*Sets a new filename*/
myCrawledLink.setPriority(myString);
myCrawledLink.setProperty(myString, myObject);
myCrawledLink.setSessionProperty(myString, myObject);
var myString = myCrawledLink.toString();

### CrawledPackage

var myBoolean = myCrawledPackage.equals(myObject);
var myLong = myCrawledPackage.getAddedDate();
var myArchive[] = myCrawledPackage.getArchives();
var myLong = myCrawledPackage.getBytesTotal();
var myString = myCrawledPackage.getComment();
var myString = myCrawledPackage.getDownloadFolder();
var myCrawledLink[] = myCrawledPackage.getDownloadLinks();
var myString = myCrawledPackage.getName();
var myString = myCrawledPackage.getPriority();
var myString = myCrawledPackage.getUUID();
var myInt = myCrawledPackage.hashCode();
var myBoolean = myCrawledPackage.remove();
myCrawledPackage.setComment(myString);
myCrawledPackage.setDownloadFolder(myString);
myCrawledPackage.setName(myString);
myCrawledPackage.setPriority(myString);
var myString = myCrawledPackage.toString();

### CrawlerJob
var myBoolean = myCrawlerJob.equals(myObject);
var myString = myCrawlerJob.getOrigin();
var myString = myCrawlerJob.getPassword();
var myString = myCrawlerJob.getSourceUrl();
var myString = myCrawlerJob.getText();
var myInt = myCrawlerJob.hashCode();
var myBoolean = myCrawlerJob.isDeepAnalysisEnabled();
myCrawlerJob.setDeepAnalysisEnabled(myBoolean);
myCrawlerJob.setPassword(myString);
myCrawlerJob.setText(myString);

### DownloadLink
myDownloadLink.abort();
var myBoolean = myDownloadLink.equals(myObject);
var myLong = myDownloadLink.getAddedDate();
var myArchive = myDownloadLink.getArchive();
var myLong = myDownloadLink.getBytesLoaded();
var myLong = myDownloadLink.getBytesTotal();
var myString = myDownloadLink.getComment();
var myString = myDownloadLink.getContainerURL();
var myString = myDownloadLink.getContentURL();
var myLong = myDownloadLink.getDownloadDuration();
var myString = myDownloadLink.getDownloadPath();
var myLong = myDownloadLink.getDownloadSessionDuration();
var myLong = myDownloadLink.getDownloadTime();
var myLong = myDownloadLink.getEta();
var myString = myDownloadLink.getExtractionStatus();
var myString = myDownloadLink.getFinalLinkStatus();
var myLong = myDownloadLink.getFinishedDate();
var myString = myDownloadLink.getHost();
var myString = myDownloadLink.getName();
var myString = myDownloadLink.getOriginURL();
var myFilePackage = myDownloadLink.getPackage();
var myString = myDownloadLink.getPriority();
var myObject = myDownloadLink.getProperty(myString);
var myString = myDownloadLink.getReferrerURL();
var myObject = myDownloadLink.getSessionProperty(myString);
var myString = myDownloadLink.getSkippedReason();
var myLong = myDownloadLink.getSpeed();
var myString = myDownloadLink.getStatus();
var myString = myDownloadLink.getUUID();
var myString = myDownloadLink.getUrl();
var myInt = myDownloadLink.hashCode();
var myBoolean = myDownloadLink.isEnabled();
var myBoolean = myDownloadLink.isFinished();
var myBoolean = myDownloadLink.isResumeable();
var myBoolean = myDownloadLink.isRunning();
var myBoolean = myDownloadLink.isSkipped();
var myBoolean = myDownloadLink.remove();
myDownloadLink.reset();
myDownloadLink.resume();
myDownloadLink.setComment(myString);
myDownloadLink.setEnabled(myBoolean);
myDownloadLink.setName(myString/*new Name*/);/*Sets a new filename*/
myDownloadLink.setPriority(myString);
myDownloadLink.setProperty(myString, myObject);
myDownloadLink.setSessionProperty(myString, myObject);
myDownloadLink.setSkipped(myBoolean);
var myString = myDownloadLink.toString();

### Browser
var myBrowser = myBrowser.cloneBrowser();
var myBoolean = myBrowser.equals(myObject);
var myString = myBrowser.getHTML();
var myString = myBrowser.getPage(myString);
var myString = myBrowser.getURL();
var myInt = myBrowser.hashCode();
var myString = myBrowser.postPage(myString, myString);

### Archive
var myBoolean = myArchive.equals(myObject);
var myString = myArchive.getArchiveType();
var myDownloadLink[] = myArchive.getDownloadLinks();
var myString = myArchive.getExtractToFolder();
var myFilePath[] = myArchive.getExtractedFilePaths();
var myString[] = myArchive.getExtractedFiles();
var myString = myArchive.getExtractionLog();
var myString = myArchive.getFolder();
var myObject = myArchive.getInfo();
var myString = myArchive.getName();
var myString = myArchive.getUsedPassword();
var myInt = myArchive.hashCode();
var myBoolean = myArchive.isPasswordProtected();



## 流程-初始化
* HttpAPIExtension.initExtension()
* HttpAPIExtension.start()
* HttpAPIExtension.startServer()
    * LinkCollector.getInstance() 取得 JDownloader 的 LinkCollector
    * 用 LinkCollector 初始化 LinkController
    * 用 LinkController 初始化 JDServerGETHandler, JDServerPOSTHandler
    * jetty server 初始化
        * 添加 JDServerGETHandler, JDServerPOSTHandler
    * jetty server 啟動
    
## 流程-/addLink
* BaseHandler.handle()
    * JDServerPOSTHandler.parseAddLinkParams()
    * JDLinkController.AddLink()
        * new LinkCollectingJob()
        * LinkCollector.addCrawlerJob()
        
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

## EventScripter

```
function L(msg) {
   writeFile("d:\\jd-debug.txt", JSON.stringify(msg) + "\n", true);
}

var links = getAllCrawledLinks();
for(var i=0, c=links.length;i<c;++i){
   L(links[i].getOriginURL() || links[i].getUrl());
   
   L(links[i].getContentURL());
   
   L(links[i].getAvailableState());
   L("-----");
}
```

# 參考資料
* [The JDownloader Open Source Project on Open Hub](https://www.openhub.net/p/jdownloader)
* [The JDownloader Open Source Project on Open Hub : Code Locations Page](https://www.openhub.net/p/jdownloader/enlistments)
* [JDownloader.org - Official Homepage](http://jdownloader.org/knowledge/wiki/development/get-started)
* [JDownloader - Development](http://beta.jdownloader.org/development)