//jDownloader - Downloadmanager
//Copyright (C) 2009  JD-Team support@jdownloader.org
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jd.plugins.hoster;

import java.io.IOException;

import jd.PluginWrapper;
import jd.config.ConfigContainer;
import jd.config.ConfigEntry;
import jd.http.Browser;
import jd.http.Browser.BrowserException;
import jd.http.URLConnectionAdapter;
import jd.nutils.encoding.Encoding;
import jd.parser.Regex;
import jd.plugins.DownloadLink;
import jd.plugins.DownloadLink.AvailableStatus;
import jd.plugins.HostPlugin;
import jd.plugins.LinkStatus;
import jd.plugins.PluginException;
import jd.plugins.PluginForHost;
import jd.utils.locale.JDL;

@HostPlugin(revision = "$Revision: 34711 $", interfaceVersion = 2, names = { "gamer.nl" }, urls = { "http://(www\\.)?gamer\\.nl/(video/|game/[a-z0-9\\-]+/[a-z0-9\\-]+/videos/)\\d+" })
public class GamerNl extends PluginForHost {

    public GamerNl(PluginWrapper wrapper) {
        super(wrapper);
        setConfigElements();
    }

    private final String DATE_IN_FILENAME = "DATE_IN_FILENAME";
    private final String FID_IN_FILENAME  = "FID_IN_FILENAME";
    private String       dllink           = null;

    @Override
    public String getAGBLink() {
        return "http://www.gamer.nl/redactiestatuut";
    }

    @Override
    public AvailableStatus requestFileInformation(final DownloadLink downloadLink) throws IOException, PluginException {
        this.setBrowserExclusive();
        br.setFollowRedirects(true);
        br.getPage(downloadLink.getDownloadURL() + "?hd");
        if (br.getHttpConnection().getResponseCode() == 404 || !br.containsHTML("new SWFObject")) {
            throw new PluginException(LinkStatus.ERROR_FILE_NOT_FOUND);
        }
        final String date = br.getRegex("<div class=\"sub\">[\t\n\r ]+([^<>\"/]*?) /").getMatch(0);
        final String xml = br.getRegex("\"(/video/playlist/\\d+(/(hd|sd))?\\.xml)\"").getMatch(0);
        if (xml == null || date == null) {
            throw new PluginException(LinkStatus.ERROR_PLUGIN_DEFECT);
        }
        br.getPage("http://www.gamer.nl" + xml);
        String filename = br.getRegex("<title>([^<>]*?)</title>").getMatch(0);
        dllink = br.getRegex("<media:content url=\"(https?://[^<>\"]*?)\" />").getMatch(0);
        if (filename == null || dllink == null) {
            throw new PluginException(LinkStatus.ERROR_PLUGIN_DEFECT);
        }
        dllink = Encoding.htmlDecode(dllink);
        filename = Encoding.htmlDecode(filename);
        filename = filename.trim();
        filename = encodeUnicode(filename);
        final String ext = getFileNameExtensionFromString(dllink, ".mp4");
        if (this.getPluginConfig().getBooleanProperty(DATE_IN_FILENAME, false)) {
            filename = encodeUnicode(date) + "_" + filename + ext;
        } else {
            filename = filename + ext;
        }
        if (this.getPluginConfig().getBooleanProperty(FID_IN_FILENAME, false)) {
            filename = new Regex(downloadLink.getDownloadURL(), "(\\d+)$").getMatch(0) + "_" + filename;
        }
        downloadLink.setFinalFileName(filename);
        final Browser br2 = br.cloneBrowser();
        // In case the link redirects to the finallink
        br2.setFollowRedirects(true);
        URLConnectionAdapter con = null;
        try {
            try {
                con = br2.openGetConnection(dllink);
            } catch (final BrowserException e) {
                throw new PluginException(LinkStatus.ERROR_FILE_NOT_FOUND);
            }
            if (!con.getContentType().contains("html")) {
                downloadLink.setDownloadSize(con.getLongContentLength());
            } else {
                throw new PluginException(LinkStatus.ERROR_FILE_NOT_FOUND);
            }
            return AvailableStatus.TRUE;
        } finally {
            try {
                con.disconnect();
            } catch (final Throwable e) {
            }
        }
    }

    @Override
    public void handleFree(final DownloadLink downloadLink) throws Exception {
        requestFileInformation(downloadLink);
        dl = jd.plugins.BrowserAdapter.openDownload(br, downloadLink, dllink, true, 0);
        if (dl.getConnection().getContentType().contains("html")) {
            br.followConnection();
            throw new PluginException(LinkStatus.ERROR_PLUGIN_DEFECT);
        }
        dl.startDownload();
    }

    private void setConfigElements() {
        this.getConfig().addEntry(new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, this.getPluginConfig(), DATE_IN_FILENAME, JDL.L("plugins.hoster.GamerNl.dateInFilename", "Include date in filename?")).setDefaultValue(false));
        this.getConfig().addEntry(new ConfigEntry(ConfigContainer.TYPE_CHECKBOX, this.getPluginConfig(), FID_IN_FILENAME, JDL.L("plugins.hoster.GamerNl.fidInFilename", "Include video id in filename?")).setDefaultValue(false));
    }

    @Override
    public int getMaxSimultanFreeDownloadNum() {
        return -1;
    }

    @Override
    public void reset() {
    }

    @Override
    public void resetPluginGlobals() {
    }

    @Override
    public void resetDownloadlink(DownloadLink link) {
    }
}
