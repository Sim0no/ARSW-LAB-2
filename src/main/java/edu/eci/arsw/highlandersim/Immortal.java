package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;;
public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;

    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;
    private final List<Immortal> immortalsPopulationP;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    private AtomicBoolean alive,running;



    public Immortal(String name, List<Immortal> immortalsPopulation,  List<Immortal> immortalsPP,int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.alive = new AtomicBoolean(true);
        this.immortalsPopulationP=immortalsPP;
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {
        this.running = new AtomicBoolean(true);
        while (alive.get()) {
            while(!running.get()){
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getHealth().get() <= 0){
                setAlive(false);
                getHealth().set(0);
                immortalsPopulation.remove(this);



            }
            else{
                Immortal im=null;

                int myIndex = immortalsPopulation.indexOf(this);

                int nextFighterIndex = r.nextInt(immortalsPopulation.size());

                //avoid self-fight
                while(im==null) {
                    if (nextFighterIndex == myIndex) {
                        nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                    }
                    im = immortalsPopulation.get(nextFighterIndex);
                    if(!im.immortalIsAlive()){
                        im=null;
                    }
                    else{
                        this.fight(im);
                    }

                }







            }
        }
    }

    public void fight(Immortal i2) {
        boolean valid_attack=false;
        synchronized (i2){
            valid_attack = i2.getHealth().get() > 0; //is a valid attack if the other immortal is alive
            if (valid_attack){
                i2.getHealth().getAndAdd(-defaultDamageValue);
            }
            else{
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
                return;
            }

        }
        synchronized (this){
            if(valid_attack){
                this.health.getAndAdd(defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            }
        }

    }

    public void changeHealth(int v) {
        health.set(v);
    }

    public AtomicInteger getHealth() {
        return health;
    }


    public boolean immortalIsAlive() {
        return alive.get();
    }

    public void setAlive(boolean alive) {
        this.alive.set(alive);
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }
    public void sleepImmortal(){
        this.running.set(false);
    }
    public synchronized void wakeUpImmortal(){
        this.running.set(true);
        this.notify();


    }

}
