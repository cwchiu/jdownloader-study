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
import jd.plugins.FilePackage;
import jd.plugins.PluginForDecrypt;

@DecrypterPlugin(revision = "$Revision: 34675 $", interfaceVersion = 3, names = { "flog.pl" }, urls = { "https?://(?:www\\.)?[^/]+\\.flog\\.pl/archiwum" }) 
public class FlogPlDecrypter extends PluginForDecrypt {

    public FlogPlDecrypter(PluginWrapper wrapper) {
        super(wrapper);
    }

    public ArrayList<DownloadLink> decryptIt(CryptedLink param, ProgressController progress) throws Exception {
        ArrayList<DownloadLink> decryptedLinks = new ArrayList<DownloadLink>();
        final String parameter = param.toString();
        br.getPage(parameter);
        if (br.getHttpConnection().getResponseCode() == 404 || this.br.containsHTML("class=\"messages\"")) {
            decryptedLinks.add(this.createOfflinelink(parameter));
            return decryptedLinks;
        }
        final String username = new Regex(parameter, "(?:www\\.)?([^/]+)\\.flog\\.pl/").getMatch(0);
        final String total_imgs_num_str = this.br.getRegex(">Archiwum<sup style=\"[^<>\"]+\">\\((\\d+)\\)</sup>").getMatch(0);
        if (total_imgs_num_str == null) {
            return null;
        }
        final long entries_per_page = 50;
        long imgs_decrypted_num = 0;
        final long total_imgs_num = Long.parseLong(total_imgs_num_str);
        int page = 0;

        final FilePackage fp = FilePackage.getInstance();
        fp.setName(username);

        if (total_imgs_num == 0) {
            decryptedLinks.add(this.createOfflinelink(parameter));
            return decryptedLinks;
        }

        this.br.getHeaders().put("X-Requested-With", "XMLHttpRequest");

        do {
            if (this.isAbort()) {
                logger.info("Decryption aborted by user");
            }
            this.br.getPage("http://" + username + ".flog.pl/archiwum_ajax/0/0/0/wgdaty/" + page);
            final String[] links = br.getRegex("id=\"archimg(\\d+)\"").getColumn(0);
            if (links == null || links.length == 0) {
                /* Fail safe */
                break;
            }

            for (final String linkid : links) {
                final String url_content = "http://" + username + ".flog.pl/wpis/" + linkid + "/";
                final String linkid_intern = username + "_" + linkid;
                final DownloadLink dl = createDownloadlink(url_content);
                dl.setLinkID(linkid_intern);
                dl.setName(linkid_intern + jd.plugins.hoster.FlogPl.default_Extension);
                dl._setFilePackage(fp);
                dl.setAvailable(true);
                decryptedLinks.add(createDownloadlink(linkid));
                distribute(dl);
                imgs_decrypted_num++;
            }

            if (links.length < entries_per_page) {
                /* Fail safe */
                break;
            }

            logger.info("Decrypted links: " + decryptedLinks.size() + " / " + total_imgs_num);
            page++;
        } while (imgs_decrypted_num < total_imgs_num);

        if (decryptedLinks.size() == 0 && !this.isAbort()) {
            return null;
        }

        return decryptedLinks;
    }

}
