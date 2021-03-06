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
import jd.parser.Regex;
import jd.plugins.CryptedLink;
import jd.plugins.DecrypterPlugin;
import jd.plugins.DownloadLink;
import jd.plugins.PluginForDecrypt;

@DecrypterPlugin(revision = "$Revision: 34675 $", interfaceVersion = 2, names = { "break.com" }, urls = { "http://(www\\.)?(break\\.com/(index|usercontent|skittles|video)/[A-Za-z0-9\\-_/]+\\-\\d+$|break\\.com/embed/\\d+)" }) 
public class BreakComDecrypter extends PluginForDecrypt {

    public BreakComDecrypter(PluginWrapper wrapper) {
        super(wrapper);
    }

    public ArrayList<DownloadLink> decryptIt(CryptedLink param, ProgressController progress) throws Exception {
        ArrayList<DownloadLink> decryptedLinks = new ArrayList<DownloadLink>();
        br.setFollowRedirects(true);
        final String parameter = param.toString();
        final String fid = new Regex(parameter, "(\\d+)$").getMatch(0);
        br.getPage("http://www.break.com/embed/" + fid);
        br.getRequest().setHtmlCode(br.toString().replace("\\", ""));
        String externID = br.getRegex("(http://(www\\.)?youtube\\.com/[^<>\"]*?)\"").getMatch(0);
        if (externID != null) {
            decryptedLinks.add(createDownloadlink(externID));
            return decryptedLinks;
        }
        externID = br.getRegex("=\"(//player\\.vimeo\\.com/video/\\d+)").getMatch(0);
        if (externID != null) {
            decryptedLinks.add(createDownloadlink("http:" + externID));
            return decryptedLinks;
        }
        decryptedLinks.add(createDownloadlink("http://breakdecrypted.com/video/xyz-" + fid));

        return decryptedLinks;
    }

    /* NO OVERRIDE!! */
    public boolean hasCaptcha(CryptedLink link, jd.plugins.Account acc) {
        return false;
    }

}