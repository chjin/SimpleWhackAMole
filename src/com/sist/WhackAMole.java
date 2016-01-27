package com.sist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hojin on 16. 1. 26.
 */
public class WhackAMole extends JFrame{
    public Boolean gameRunning=false;
    private int gridSize=4;
    private Mole[][] moles=new Mole[gridSize][gridSize];
    private int moleRow,moleCol;

    public int score=0;
    private int intervalMillisecs=1000;
    private String scoresFilePath="/home/hojin/IdeaProjects/SimpleWhackAMole/src/com/sist/resource/scores.txt";

    private JPanel gameJPanel=new JPanel();
    Timer myTimer;

    //점수와 컨트롤들이 있는 가장 상위 panel.
    JPanel jPanel=new JPanel();

    private JPanel contentPane;

    JLabel scoreJLabel=new JLabel("점수: " +score);
    String[] difficulties={"쉬움","중간","어려움"};

    private final JComboBox comboBox=new JComboBox(difficulties);
    private final JButton btnStartGame=new JButton("게임 시작");
    private final JButton btnEndGame=new JButton("게임 끝냄");


    /*
    - GUI 응용 프로그램에서 마우스등 움직임==>MouseEvent 클래스의 인스턴스가 만들어짐.
      그리고  각각의 인스턴스는 Swing 내부 이벤트 큐에 저장됨.
    ==>........

    main() 메소드가 처음으로 하는 것
    ==> Runnable 오브젝트 생성,
        JFrame 생성,
        이 프레임에 모든 컴포넌트 삽입함.
     */
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
           public void run(){
               try{
                   WhackAMole frame= new WhackAMole();
                   frame.setVisible(true);
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
        });
    }

    /*
    생성자 - frame 생성.
     */
    public WhackAMole(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,450,300);

        contentPane=new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0,0));

        //whackamole를 위한 4 by 4 그리드를 갖는 패널.
        gameJPanel.setLayout(new GridLayout(4,4));
        contentPane.add(gameJPanel, BorderLayout.CENTER);

        contentPane.add(jPanel, BorderLayout.NORTH);
        jPanel.add(scoreJLabel);


        comboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                intervalMillisecs=1000-(comboBox.getSelectedIndex() *150);
                myTimer.cancel();

                initialiseGamePlay();
                //순서 섞기
                timedShamble();
            }
        });

        jPanel.add(comboBox);

        btnStartGame.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                gameRunning=true;
            }
        });

        btnEndGame.addActionListener(new ActionListener() {
            //기록 저장
            @Override
            public void actionPerformed(ActionEvent e) {
                try(PrintWriter printWriter=new PrintWriter(
                        new BufferedWriter(new FileWriter(scoresFilePath,true))
                )){
                    printWriter.println("날짜: " +getDate()+ ", 시간: " +getTime()+ "\n"
                                        +"점수: " +score+ "\n");
                    printWriter.flush();
                    printWriter.close();

                }catch(IOException e1){
                    e1.printStackTrace();
                }

                gameRunning=false;
                score=0;
                scoreJLabel.setText("점수: " +score);
            }
        });

        jPanel.add(btnStartGame);
        jPanel.add(btnEndGame);

        initialiseGamePlay();
        timedShamble();

    }

    private void initialiseGamePlay(){
        Random random=new Random();
        moleRow=random.nextInt(gridSize);
        moleCol=random.nextInt(gridSize);

        for(int row=0; row<moles.length; row++){
            for(int col=0; col<moles.length; col++){
                moles[row][col]=new Mole(false);
                moles[row][col].addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getSource() instanceof Mole){
                            if(gameRunning){
                                Mole currentMole=(Mole)e.getSource();
                                if(currentMole.getActive()){
                                    score +=1;
                                    currentMole.setActive(false);
                                }else{
                                    score -=1;
                                }
                                scoreJLabel.setText("점수: " +score);
                            }
                        }else{
                            throw new IOError(null);
                        }
                    }
                });

            }
        }
        moles[moleRow][moleCol].setActive(true);
        setButtons();
    }

    private void setButtons(){
        gameJPanel.removeAll();
        for(int row=0; row<moles.length; row++){
            for(int col=0; col<moles[row].length; col++){
                String address="cell " +row+ " " +col;
                gameJPanel.add(moles[row][col], address);
            }
        }
    }

    private void timedShamble(){
        myTimer=new Timer();
        myTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(gameRunning){
                    shamble();
                }
            }
        },0,intervalMillisecs);
    }

    private void shamble(){
        moles[moleRow][moleCol].setActive(false);

        Random random=new Random();
        moleRow=random.nextInt(gridSize);
        moleCol=random.nextInt(gridSize);

        moles[moleRow][moleCol].setActive(true);
    }

    private String getDate(){
        Calendar calendar=Calendar.getInstance();
        calendar.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }

    private String getTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }
}















































