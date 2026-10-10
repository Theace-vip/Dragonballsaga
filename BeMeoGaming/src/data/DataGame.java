package data;



import models.Template.HeadAvatar;
import models.Template.MapTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.FileIO;
import services.Service;
import skill.NClass;
import skill.Skill;
import models.Template.MobTemplate;
import models.Template.NpcTemplate;
import models.Template.SkillTemplate;
import network.inetwork.ISession;
import network.Message;

import java.io.IOException;

import server.Manager;
import server.io.MySession;
import utils.Logger;
import utils.Util;
import models.Template.BgItem;
import power.Caption;
import power.CaptionManager;

public class DataGame {

    // [Cai trang Goku Hac Am 26/09/2026] tang version de client tai lai file part (them part 2140-2142)
    // v12: chinh sua dx/dy part 2140-2142 (khoang cach dau/than/chan)
    // [Cai trang VLT 052 05/10/2026] v14: them part 2143-2145 (Cai trang VLT 052 - spine g13_juitianxuannv,
    // chieu cao theo frame spine goc stand-1/f0 = 446px) -> client tai lai du lieu part.
    // [Cai trang VLT 021 05/10/2026] v15: them part 2146-2148 (Cai trang VLT 021 - spine g13_dijiang,
    // chieu cao muc tieu 150px) -> client tai lai du lieu part.
    // [Cai trang VLT 090 05/10/2026] v16: them part 2149-2151 (Cai trang VLT 090 - spine g13_qiongqi_nan,
    // chieu cao muc tieu 150px) -> client tai lai du lieu part.
    public static byte vsData = 16;
    public static byte vsMap = 2;
    public static byte vsSkill = 1;
    // [Cung Menh 24/09/2026] tang version khi them item 1912 (Manh Tinh Tu) + 1913 (Ngoc Tinh Do)
    // [Ban Nguyen 10/10/2026] tang tiep khi them item 1942 (Manh Vo Tinh Thach) + 1943 (Hat Giong Khoi Nguyen)
    // de client cu xoa cache item template trong RMS (NRitem0/1/2) va tai lai danh sach moi.
    // Neu khong tang, client se dung cache cu -> khong biet template 1912/1913 -> item trong hanh trang
    // bi loi khi ve (NullReferenceException) nen khong hien ra.
    // [Cai trang Goku Hac Am 26/09/2026] tang version -> client tai lai template (them item 1915)
    // [The Diem Farm 27/09/2026] tang version -> client xoa cache item template, tai lai danh sach
    // (them item 1916/1917/1918 = the x2/x3/x5 diem farm; 32326 = anh Ve Tang Diem 1251).
    // Khong tang thi client dung cache cu -> khong biet template moi -> ve bi loi khi ve.
    // v15: sua icon_id cua 1251 (Ve Tang Diem) -> 32326.
    // v16: them item 1918 (The X5 Diem Farm, icon 32327).
    // v17: doi TYPE 1929 (Hao Quang Hon Don Vo Cuc) tu 5 -> 11 (he thong Hao quang/flag_bag),
    //      them flag_bag 193 + icon 32339..32346 -> client xoa cache template va tai lai.
    // v19: them item 1931 Cuu Thien Thanh Long, flag_bag 195 va icon 32372..32414.
    // v20: remap 1931 sang frame procedural (GenOrbitDragon) 32415..32456 + icon 32457,
    //      xoa bo artwork deformer cu -> client tai lai icon/template.
    // v21: them item 1935 Cai trang VLT 052 (TYPE=5, head/body/leg 2143/2144/2145,
    //      icon 32635, avatar 32636) + doi ten 1934 -> "Ngọc Thố Tinh" (co dau).
    // v22: them item 1936 Cai trang VLT 021 (TYPE=5, head/body/leg 2146/2147/2148, icon 32671, avatar 32672).
    // v23: them item 1937 Cai trang VLT 090 (TYPE=5, head/body/leg 2149/2150/2151, icon 32707, avatar 32708).
    // v24: doi 14 o vong quay Tam Bao sang vat pham moi (224,220,221,222,223,457,1225,1224,1913,1588,1270,
    //      1271,1150,884,1810,531,1753) -> tang version de client xoa cache item template trong RMS
    //      (NRitem0/1/2) va tai lai danh sach moi truoc khi mo vong quay.
    // v25: them item 1942 (Manh Vo Tinh Thach, icon 32745) + 1943 (Hat Giong Khoi Nguyen, icon 32746)
    //      cho Ban Nguyen Tinh Cau -> client xoa cache item template trong RMS (NRitem0/1/2) va tai lai.
    public static byte vsItem = 25;
    public static int vsRes = 40;
    public static short maxSmallVersion = 32767;

    public static String LINK_IP_PORT = "LOCAL:127.0.0.1:14445:0";
    public static Map MAP_MOUNT_NUM = new HashMap();

    public static void sendVersionGame(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 4);
            msg.writer().writeByte(vsData);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(vsItem);
            msg.writer().writeByte(0);
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg.writer().writeLong(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeDouble(caption.getPower());
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    /**
     * [27/09/2026] Keo dai bang version icon - data/smallimage_version/x{1..4}/smallimage_version_data.
     *
     * Cau truc file (da verify tren file that): short BE = maxVersion (maxIconId + 1),
     * sau do tung byte = len(data/icon_botnet/x{z}/{id}.png) % 127, thieu file = -1.
     * Client nhan file nay qua msg -77 khi LOGIN -> neu icon_id ngoai bang thi client
     * khong biet co icon moi -> o do trong hanh trang (loi khi them 32324/32325/32326).
     *
     * Chi BO SUNG phan cuoi + doi header, giu nguyen du lieu da co -> client cu van dung duoc.
     * Duoc goi 1 lan trong Manager luc boot, nen them icon xong chi can restart la du.
     */
    public static void extendSmallImageVersion() {
        try {
            int maxIcon = -1;
            File[] fs = new File("data/icon_botnet/x2").listFiles();
            if (fs == null) {
                return;
            }
            for (File f : fs) {
                String n = f.getName();
                if (!n.endsWith(".png")) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(n.substring(0, n.length() - 4));
                    if (id > maxIcon) {
                        maxIcon = id;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            if (maxIcon < 0) {
                return;
            }
            int newMax = maxIcon + 1;
            for (int z = 1; z <= 4; z++) {
                File f = new File("data/smallimage_version/x" + z + "/smallimage_version_data");
                if (!f.isFile()) {
                    continue;
                }
                byte[] old = java.nio.file.Files.readAllBytes(f.toPath());
                if (old.length < 2) {
                    continue;
                }
                int header = ((old[0] & 255) << 8) | (old[1] & 255);
                if (header >= newMax) {
                    continue; // da du roi
                }
                int dataLen = old.length - 2;
                byte[] out = new byte[2 + newMax];
                out[0] = (byte) ((newMax >> 8) & 255);
                out[1] = (byte) (newMax & 255);
                int giu = Math.min(dataLen, newMax);
                System.arraycopy(old, 2, out, 2, giu); // giu nguyen du lieu cu
                for (int i = giu; i < newMax; i++) {
                    out[2 + i] = iconByte(z, i);
                }
                try {
                    java.nio.file.Files.copy(f.toPath(), java.nio.file.Paths.get(f.getPath() + ".bak"),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ignored) {
                }
                java.nio.file.Files.write(f.toPath(), out);
                Logger.success("Keo dai smallimage_version x" + z + ": " + header + " -> " + newMax
                        + " (icon_id moi toi da: " + maxIcon + ")\n");
            }
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    private static byte iconByte(int zoom, int id) {
        File f = new File("data/icon_botnet/x" + zoom + "/" + id + ".png");
        if (!f.isFile()) {
            return -1;
        }
        return (byte) (f.length() % 127);
    }

    //vData
    public static void updateData(MySession session) {
        final byte[] dart = FileIO.readFile("data/update_data/dart");
        final byte[] arrow = FileIO.readFile("data/update_data/arrow");
        final byte[] effect = FileIO.readFile("data/update_data/effect");
        final byte[] image = FileIO.readFile("data/update_data/image");
        final byte[] part = FileIO.readFile("data/update_data/part");
        final byte[] skill = FileIO.readFile("data/update_data/skill");

        Message msg;
        try {
            msg = new Message(-87);
            msg.writer().writeByte(vsData);
            msg.writer().writeInt(dart.length);
            msg.writer().write(dart);
            msg.writer().writeInt(arrow.length);
            msg.writer().write(arrow);
            msg.writer().writeInt(effect.length);
            msg.writer().write(effect);
            msg.writer().writeInt(image.length);
            msg.writer().write(image);
            msg.writer().writeInt(part.length);
            msg.writer().write(part);
            msg.writer().writeInt(skill.length);
            msg.writer().write(skill);

            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //vMap
    public static void updateMap(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 6);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(Manager.MAP_TEMPLATES.length);
            for (MapTemplate temp : Manager.MAP_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
            }
            msg.writer().writeByte(Manager.NPC_TEMPLATES.size());
            for (NpcTemplate temp : Manager.NPC_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
                msg.writer().writeShort(temp.head);
                msg.writer().writeShort(temp.body);
                msg.writer().writeShort(temp.leg);
                msg.writer().writeByte(0);
            }
            msg.writer().writeByte(Manager.MOB_TEMPLATES.size());
            for (MobTemplate temp : Manager.MOB_TEMPLATES) {
                msg.writer().writeByte(temp.type);
                msg.writer().writeUTF(temp.name);
                if(session.version == 237){
                    msg.writer().writeDouble(temp.hp);
                }else{
                    msg.writer().writeDouble((temp.hp));
                }
                
                msg.writer().writeByte(temp.rangeMove);
                msg.writer().writeByte(temp.speed);
                msg.writer().writeByte(temp.dartType);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    //vSkill
    public static void updateSkill(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);

            msg.writer().writeByte(7);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(0); //count skill option

            msg.writer().writeByte(Manager.NCLASS.size());
            for (NClass nClass : Manager.NCLASS) {
                msg.writer().writeUTF(nClass.name);

                msg.writer().writeByte(nClass.skillTemplatess.size());
                for (SkillTemplate skillTemp : nClass.skillTemplatess) {
                    msg.writer().writeByte(skillTemp.id);
                    msg.writer().writeUTF(skillTemp.name);
                    msg.writer().writeByte(skillTemp.maxPoint);
                    msg.writer().writeByte(skillTemp.manaUseType);
                    msg.writer().writeByte(skillTemp.type);
                    msg.writer().writeShort(skillTemp.iconId);
                    msg.writer().writeUTF(skillTemp.damInfo);
                    msg.writer().writeUTF("dragonballsaga");
                    if (skillTemp.id != 0) {
                        msg.writer().writeByte(skillTemp.skillss.size());
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                    } else {
                        //Thêm 2 skill trống 105, 106
                        msg.writer().writeByte(skillTemp.skillss.size() + 2);
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                        for (int i = 105; i <= 106; i++) {
                            msg.writer().writeShort(i);
                            msg.writer().writeByte(0);
                            msg.writer().writeLong(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeInt(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeByte(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeUTF("");
                        }
                    }
                }
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendDataImageVersion(MySession session) {
        Message msg;
        try {
//            msg = new Message(-111);
//            msg.writer().writeShort(0);
//            msg.writer().writeUTF("DragonBoy");
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("DragonBoy");
//            msg.writer().writeByte(1);
//            msg.writer().writeUTF("VuDangCapVaiLonRaMaBanDeoBietThoiDitMeBan");
//            msg.writer().writeByte(2);
//            session.doSendMessage(msg);
//            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendEffectTemplate(MySession session, int id, int... idtemp) {        int idT = id;

        if (idtemp.length > 0 && idtemp[0] != 0) {
            idT = idtemp[0];
        }
        if (idT < 0) {
            // client gui -66 id am = tin hieu xoa hieu ung (vd xoa aura) - khong co file nao
            System.out.println("[EFF] skip clear id=" + id);
            return;
        }
        Message msg;
        try {
            final byte[] effData = FileIO.readFile("data/effdata/DataEffect_" + idT);
            final byte[] effImg = FileIO.readFile("data/effect/x" + session.zoomLevel + "/ImgEffect_" + idT + ".png");
            if (effData == null || effImg == null) {
                return;
            }
            msg = new Message(-66);
            msg.writer().writeShort(id);
            msg.writer().writeInt(effData.length);
            msg.writer().write(effData);
            if (session.version > 216) {
                msg.writer().write(idT == 60 ? 2 : 0);
            }
            msg.writer().writeInt(effImg.length);
            msg.writer().write(effImg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      public static void sendEffectTemplate(MySession session, int id) {
        Message msg;
        try {
            byte[] eff_data = FileIO.readFile("Eff/effdata/x" + session.zoomLevel + "/" + id);
            msg = new Message(-66);
            msg.writer().write(eff_data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    } 
    
     public static void effData(MySession session, int id, int... idtemp) {
        
         if (session != null) {
            if(id == 200){
                sendEffectTemplate(session, id);
            }
        
        int idT = id;
        
        if (idtemp.length > 0 && idtemp[0] != 0) {
            idT = idtemp[0];
        }
        Message msg;
        try {
            byte[] effData = FileIO.readFile("Eff/effect/x" + session.zoomLevel + "/data/DataEffect_" + idT);
            byte[] effImg = FileIO.readFile("Eff/effect/x" + session.zoomLevel + "/img/ImgEffect_" + idT + ".png");
            System.out.println("[EFF] send id=" + id + " idT=" + idT + " zoom=" + session.zoomLevel
                    + " data=" + (effData == null ? -1 : effData.length) + "B img="
                    + (effImg == null ? -1 : effImg.length) + "B");
            if (effData == null || effImg == null) {
                return;
            }
            msg = new Message(-66);
            msg.writer().writeShort(id);
            msg.writer().writeInt(effData.length);
            msg.writer().write(effData);
            msg.writer().writeByte(0);
            msg.writer().writeInt(effImg.length);
            msg.writer().write(effImg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("[EFF] FAIL id=" + id);
            e.printStackTrace();
        }
    }
    }     
      
      
      
    
    public static void sendBgItemVersion(MySession session) {
        Message msg;
        try {
            msg = new Message(-93);
            msg.writer().writeShort(Manager.BG_ITEMS.size());
            for (BgItem bgItem : Manager.BG_ITEMS) {
                msg.writer().writeByte(bgItem.id);
            }
            session.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendItemBGTemplate(MySession session, int id) {
        Message msg;
        try {
            final byte[] bg_temp = FileIO.readFile("data/item_bg_temp/x" + session.zoomLevel + "/" + id + ".png");
            if (bg_temp == null) {
                return;
            }
            msg = new Message(-32);
            msg.writer().writeShort(id);
            msg.writer().writeInt(bg_temp.length);
            msg.writer().write(bg_temp);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendDataItemBG(MySession session) {
        Message msg;
        try {
            byte[] item_bg = FileIO.readFile("data/item_bg_temp/bg_data");
            msg = new Message(-31);
            msg.writer().write(item_bg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendIcon(MySession session, int id) {
        Message msg;
        try {
            final byte[] icon = FileIO.readFile("data/icon_botnet/x" + session.zoomLevel + "/" + id + ".png");
            msg = new Message(-67);
            msg.writer().writeInt(id);
            msg.writer().writeInt(icon.length);
            msg.writer().write(icon);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendSmallVersion(MySession session) {
        Message msg;
        try {
            final byte[] data= FileIO.readFile("data/smallimage_version/x" + session.zoomLevel + "/smallimage_version_data");
            msg = new Message(-77);
            msg.writer().write(data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void requestMobTemplate(MySession session, int id) {
        Message msg;
        try {
            final byte[] mob = FileIO.readFile("data/mob/x" + session.zoomLevel + "/" + id);
            msg = new Message(11);
            msg.writer().writeByte(id);
            msg.writer().write(mob);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendTileSetInfo(MySession session) {
        Message msg;
        try {
            final byte[] data = FileIO.readFile("data/map/tile_set_info");
            msg = new Message(-82);
            msg.writer().write(data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //data vẽ map
    public static void sendMapTemp(MySession session, int id) {
        Message msg;
        try {
            final byte[] data = FileIO.readFile("data/map/tile_map_data/" + id);
            if (data == null) {
                return;
            }
            msg = new Message(-28);
            msg.writer().writeByte(10);
            msg.writer().write(data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    //head-avatar
    public static void sendHeadAvatar(Message msg) {
        try {
            msg.writer().writeShort(Manager.HEAD_AVATARS.size());
            for (HeadAvatar ha : Manager.HEAD_AVATARS) {
                msg.writer().writeShort(ha.headId);
                msg.writer().writeShort(ha.avatarId);
            }
        } catch (Exception e) {
        }
    }

    public static void sendImageByName(MySession session, String imgName) {
        Message msg;
        try {
            msg = new Message(66);
            msg.writer().writeUTF(imgName);
            msg.writer().writeByte(Manager.getNFrameImageByName(imgName));
            final byte[] data = FileIO.readFile("data/img_by_name/x" + session.zoomLevel + "/" + imgName + ".png");
            msg.writer().writeInt(data.length);
            msg.writer().write(data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendVersionRes(ISession session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(0);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendSizeRes(MySession session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(1);
            final File[] files = new File("data/res/x" + session.zoomLevel).listFiles();
            if (files != null) {
                msg.writer().writeShort(files.length);
            } else {
                msg.writer().writeShort(0);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendRes(MySession session) {
        Message msg;
        try {
            for (final File fileEntry : new File("data/res/x" + session.zoomLevel).listFiles()) {
                String original = fileEntry.getName();
                byte[] res = FileIO.readFile(fileEntry.getAbsolutePath());
                msg = new Message(-74);
                msg.writer().writeByte(2);
                msg.writer().writeUTF(original);
                msg.writer().writeInt(res.length);
                msg.writer().write(res);
                session.sendMessage(msg);
                msg.cleanup();
            }

            msg = new Message(-74);
            msg.writer().writeByte(3);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendLinkIP(MySession session) {
        Message msg;
        try {
            msg = new Message(-29);
            msg.writer().writeByte(2);
            msg.writer().writeUTF(LINK_IP_PORT + ",0,0");
            msg.writer().writeByte(1);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
