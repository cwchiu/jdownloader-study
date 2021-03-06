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

package jd.plugins.decrypter;

import java.util.ArrayList;

import jd.PluginWrapper;
import jd.controlling.ProgressController;
import jd.plugins.CryptedLink;
import jd.plugins.DecrypterPlugin;
import jd.plugins.DownloadLink;

@DecrypterPlugin(revision = "$Revision: 34675 $", interfaceVersion = 3, names = { "hqporner.com" }, urls = { "https?://(?:www\\.)?hqporner\\.com/hdporn/\\d+-[A-Za-z0-9\\-_]+\\.html" }) 
public class HqpornerCom extends PornEmbedParser {

    public HqpornerCom(PluginWrapper wrapper) {
        super(wrapper);
    }

    /* DEV NOTES */
    /* Porn_plugin */

    public ArrayList<DownloadLink> decryptIt(CryptedLink param, ProgressController progress) throws Exception {
        ArrayList<DownloadLink> decryptedLinks = new ArrayList<DownloadLink>();
        final String parameter = param.toString();
        this.br.setFollowRedirects(true);
        br.getPage(parameter);
        if (br.getHttpConnection().getResponseCode() == 404) {
            decryptedLinks.add(this.createOfflinelink(parameter));
            return decryptedLinks;
        }
        String filename = br.getRegex("<h2 style=\"line\\-height: 1em;\">([^<>\"]+)<").getMatch(0);
        if (filename == null) {
            filename = br.getRegex("<title>([^<>]+) / free HD porn and sex videos on[^<>]+</title>").getMatch(0);
        }
        decryptedLinks.addAll(findEmbedUrls(filename));
        return decryptedLinks;
    }

}
