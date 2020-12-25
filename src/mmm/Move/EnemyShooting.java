package mmm;

import mmm.Util.Point;

public class EnemyShooting {
    public EnemyShoot[] lastShoots;
    int pointer=0;

    public EnemyShooting(int memorySize){
        lastShoots=new EnemyShoot[memorySize];
    }

    public void addShoot(EnemyBullets.EnemyBullet enemyBullet, double bulletHeading){
        lastShoots[pointer]=new EnemyShoot(enemyBullet,bulletHeading);
    }
    public Point whereWillHeShoot(){
        int shootCounter=0;
        for (int i = 0; i < lastShoots.length; i++) {
            if(lastShoots[i]!=null){
                shootCounter++;
               // lastShoots[i].
            }

        }
        return null;
    }
    
    public class EnemyShoot{
        EnemyBullets.EnemyBullet enemyBullet;
        double bulletheading;
        public EnemyShoot(EnemyBullets.EnemyBullet enemyBullet, double bulletHeading) {
            this.enemyBullet=enemyBullet;
            this.bulletheading=bulletHeading;
        }
    }
}
