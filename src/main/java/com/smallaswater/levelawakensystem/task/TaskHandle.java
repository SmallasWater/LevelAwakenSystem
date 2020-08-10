package com.smallaswater.levelawakensystem.task;


import com.smallaswater.levelawakensystem.data.effects.BaseBuffer;



/**
 * @author SmallasWater
 */
public class TaskHandle {

    private BaseBuffer buffer;

    private boolean isCold = false;

    private boolean close = false;

    public TaskHandle(BaseBuffer buffer){
        this.buffer = buffer;

    }


    public void runTime(){
        if(!close) {
            if (buffer.getLoadTime() > 0) {
                buffer.setLoadTime(buffer.getColdTime() - 1);
            }else{
                if(buffer.getColdTime() > 0){
                    if(!isCold){
                        isCold = true;
                    }
                    buffer.setColdTime(buffer.getColdTime() - 1);
                }else{
                    close = true;
                }
            }
        }
    }

    public boolean isCold() {
        return isCold;
    }

    public void setCold(boolean cold) {
        isCold = cold;
    }


    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public BaseBuffer getBuffer() {
        return buffer;
    }




}
