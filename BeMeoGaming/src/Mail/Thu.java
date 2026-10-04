package Mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import item.Item;

/**
 *
 * @author RAIS
 */
public class Thu {
    
    public String title;
    public String title2;
    public String note;
    public String content;
    public long time;
    public Boolean isNhan;
    public Boolean isSeen;
    
    public List<Item> listItem = new ArrayList<Item>();
    public Thu(){
        
    }
    public Thu(String title,String title2,String note,String content,long time,Boolean isNhan,Boolean isSeen){
        this.title = title;
        this.title2 = title2;
        this.note = note;
        this.time = time;
        this.isNhan = isNhan;
        this.content = content;
        this.isSeen = isSeen;
    }
    
    public String getTime(){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd-MM");
        String ans = format.format(date);
        return ans;
    }
}
