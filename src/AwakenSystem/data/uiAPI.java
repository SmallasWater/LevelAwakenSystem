package AwakenSystem.data;


import cn.nukkit.Player;

import cn.nukkit.network.protocol.ModalFormRequestPacket;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class uiAPI implements baseAPI{
    private static uiAPI api;

    public uiAPI(){
        api = this;
    }
    public static uiAPI getApi(){
        return api;
    }

    private int id;
    private LinkedHashMap<String,Object> data = new LinkedHashMap<>();


    public void sendChose(Player player){
        this.id = chose;
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String,Object>> button = new ArrayList<>();
        for(String att :defaultAPI.getFinalAwaken(player)){
            LinkedHashMap<String,Object> buttons = new LinkedHashMap<>();
            buttons.put(FromType.TEXT.getName(),att);
            buttons.put(FromType.IMAGE.getName(),defaultAPI.getImage_Awaken(att));
            button.add(buttons);
        }
        data.put(FromType.TITLE.getName(),"等级系统");
        data.put(FromType.TYPE.getName(),FromType.FROM.getName());
        data.put(FromType.CONTENT.getName(),"");
        data.put(FromType.BUTTONS.getName(),button);
        this.send(player,id,data);
    }

    public void sendChoseAtt(Player player){
        this.id = chose_Att;
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String,Object>> button = new ArrayList<>();
        for(String att :defaultAtt.keySet()){
            LinkedHashMap<String,Object> buttons = new LinkedHashMap<>(),
                                         image   = new LinkedHashMap<>();
            buttons.put(FromType.TEXT.getName(),att);
            image.put(FromType.TYPE.getName(),FromType.PATH.getName());
            image.put(FromType.DATA.getName(),defaultAtt.get(att));
            buttons.put(FromType.IMAGE.getName(),image);
            button.add(buttons);
        }
        data.put(FromType.TITLE.getName(),"等级系统");
        data.put(FromType.TYPE.getName(),FromType.FROM.getName());
        data.put(FromType.CONTENT.getName(),"");
        data.put(FromType.BUTTONS.getName(),button);
        this.send(player,id,data);

    }


    public void sendModal(Player player,String text,String button1,String button2){
        this.id = modal;
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        data.put(FromType.TYPE.getName(),FromType.LIST_MODAL.getName());
        data.put(FromType.CONTENT.getName(),text);
        data.put(FromType.BUTTON1.getName(),button1);
        data.put(FromType.BUTTON2.getName(),button2);
        data.put(FromType.TITLE.getName(),"等级系统  ");
        this.send(player,id,data);
    }

    public void sendFrom(Player player){
        this.id = from_setting;
        LinkedHashMap<String,Object> data = new LinkedHashMap<>();
        ArrayList<Map<String,Object>> button = new ArrayList<>();
        data.put(FromType.TITLE.getName(),"等级系统");
        for(String name:defaultMessage.keySet()){
            LinkedHashMap<String,Object> buttons = new LinkedHashMap<>(),
                                         image   = new LinkedHashMap<>();
            if(name.equals("天赋进阶")){
                buttons.put(FromType.TEXT.getName(),name + "(需要 "+DamageMath.getUpDataAwakenMoney(player)+")");
            }else{
                buttons.put(FromType.TEXT.getName(),name);
            }
            image.put(FromType.TYPE.getName(),FromType.PATH.getName());
            image.put(FromType.DATA.getName(),defaultMessage.get(name));
            buttons.put(FromType.IMAGE.getName(),image);
            button.add(buttons);
        }
        data.put(FromType.TYPE.getName(),FromType.FROM.getName());
        data.put(FromType.CONTENT.getName(),"请选择");
        //data.put(FromType.BUTTONS.getName(),button);
        data.put(FromType.BUTTONS.getName(),button);
        this.send(player,id,data);
    }
    public void sendMenuMessage(Player player,String text){
        this.id = 0xddefa;
        data.put(FromType.TYPE.getName(),FromType.FROM.getName());
        data.put(FromType.CONTENT.getName(),text);
        data.put(FromType.TITLE.getName(),"等级系统");
        data.put(FromType.BUTTONS.getName(),new ArrayList<>());
        this.send(player,id,data);
    }

    private void send(Player player,int id,LinkedHashMap<String,Object> data){
        ModalFormRequestPacket ui = new ModalFormRequestPacket();
        ui.formId = id;
        ui.data = new GsonBuilder().setPrettyPrinting().create().toJson(data);
        player.dataPacket(ui);
    }

}
