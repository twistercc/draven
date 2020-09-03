package com.example.designmodel.strategy;

/**
 * 具体策略类
 *
 * @author twister
 * @date 2020/8/5
 */
public class TrainStrategy implements TravelStrategy {
    @Override
    public void travleAlgorthm() {
        System.out.println("乘坐火车。。。。");
    }
}
class HighTrainStrategy implements TravelStrategy {
    @Override
    public void travleAlgorthm() {
        System.out.println("乘坐高铁。。。。");
    }
}

class AirStrategy implements TravelStrategy {
    @Override
    public void travleAlgorthm() {
        System.out.println("乘坐飞机。。。。");
    }
}
