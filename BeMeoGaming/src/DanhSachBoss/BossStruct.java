/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DanhSachBoss;

import item.Item;
import java.util.List;

/**
 *
 * @author HairMod
 */
public class BossStruct {
     public int id;
    public String name; 
    public String hp;
    public String dame; 
    public String timeAppear;
    public int[] mapAppears;
    public short head, body, leg;
    public List<Item> dropItems;
}
