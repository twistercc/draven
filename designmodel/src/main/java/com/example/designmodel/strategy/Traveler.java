package com.example.designmodel.strategy;

/**
 * 策略模式
 *  * 代替if-else代码臃肿的写法
 *  *定义一系列的算法,把每一个算法封装起来, 并且使它们可相互替换
 *  * 策略模式把对象本身和运算规则区分开来，因此我们整个模式也分为三个部分。
 *  *
 *  * 环境类(Context):用来操作策略的上下文环境，也就是我们游客。
 *  * 抽象策略类(Strategy):策略的抽象，出行方式的抽象
 *  * 具体策略类(ConcreteStrategy):具体的策略实现，每一种出行方式的具体实现。
 *
 * @author twister
 * @date 2020/8/5
 */
public class Traveler {

    TravelStrategy travelStrategy;

    public void setTravelStrategy(TravelStrategy travelStrategy) {
        this.travelStrategy = travelStrategy;
    }

    public void travelStyle(){
        travelStrategy.travleAlgorthm();
    }

    public static void main(String[] args) {
        Traveler traveler = new Traveler();
        // 出行策略
        traveler.setTravelStrategy(new TrainStrategy());
        traveler.setTravelStrategy(new HighTrainStrategy());
        traveler.setTravelStrategy(new AirStrategy());
        // 出行
        traveler.travelStyle();
    }
}
