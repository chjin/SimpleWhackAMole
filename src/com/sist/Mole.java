package com.sist;

import javax.swing.*;

/**
 * Created by hojin on 16. 1. 26.
 */
public class Mole extends JButton {
    private Boolean active=false;

    public Mole(Boolean active){
        if(active){
            setActiveImage();
        } else{
            setInactiveImage();
        }
    }

    private void setActiveImage(){
        String trollFileLocation="/home/hojin/IdeaProjects/SimpleWhackAMole/src/com/sist/resource/troll.jpg";
        ImageIcon imageIcon=new ImageIcon(trollFileLocation);
        this.setIcon(imageIcon);
    }

    private void setInactiveImage(){
        String trollFileLocation="/home/hojin/IdeaProjects/SimpleWhackAMole/src/com/sist/resource/trollHole.gif";
        ImageIcon imageIcon=new ImageIcon(trollFileLocation);
        this.setIcon(imageIcon);
    }

    public void setActive(Boolean b){
        active=b;
        if(b){
            setActiveImage();
        } else{
            setInactiveImage();
        }
    }

    public Boolean getActive(){
        return active;
    }
}































